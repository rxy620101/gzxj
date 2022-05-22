<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="申诉学年" prop="appealYear">
        <el-input
          v-model="queryParams.appealYear"
          placeholder="请输入申诉学年"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申诉学期" prop="appealTerm">
        <el-select v-model="queryParams.appealTerm" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.valid_term"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="appealStatus">
        <el-select v-model="queryParams.appealStatus" placeholder="请选择审核状态" clearable
        >
          <el-option label="待审核" value="'0'"></el-option>
          <el-option label="审核结束" value="'1'"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专业名称" prop="majorName">
        <el-select v-model="queryParams.majorName" placeholder="请选择专业名称"
        >
          <el-option
            v-for="dict in majorOptions"
            :key="dict.id"
            :label="dict.label"
            :value="dict.label"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="muplitCheck"
          :disabled="appealInfoList.length==0"
        >审核
        </el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="appealInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center"/>
      <el-table-column label="id" align="center" prop="appealId" v-if="false"/>
      <el-table-column label="学号" align="center" prop="sno"/>
      <el-table-column label="姓名" align="center" prop="stuName"/>
      <el-table-column label="专业" align="center" prop="majorName" show-overflow-tooltip/>
      <el-table-column label="学年" align="center" prop="appealYear"/>
      <el-table-column label="学期" align="center" prop="appealTerm">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.valid_term" :value="scope.row.appealTerm"/>
        </template>
      </el-table-column>
      <el-table-column label="事由" align="center" prop="appealReason" show-overflow-tooltip/>
      <el-table-column label="状态" align="center" prop="appealStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.process" :value="scope.row.appealStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="审核结果" align="center" prop="appealResult">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.check_result" :value="scope.row.appealResult"/>
        </template>
        />
      </el-table-column>
      <el-table-column label="操作" align="center" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleCheck(scope.row)"
            v-if="scope.row.appealResult=='0'"
          >审核
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDetail(scope.row)"
            v-if="scope.row.appealResult=='1'"
          >查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改成绩申诉对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row v-if="single== true">
          <el-col :span="12">
            <el-form-item label="学号" prop="sno">
              <el-input v-model="form.sno" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="stuName">
              <el-input v-model="form.stuName" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="single== true">
          <el-col :span="12">
            <el-form-item label="学年" prop="appealYear">
              <el-input v-model="form.appealYear" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学期" prop="appealTerm">
              <el-input v-model="form.appealTerm" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="single== true">
          <el-form-item label="事由" prop="appealReason">
            <el-input v-model="form.appealReason" disabled type="textarea"/>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="审核结果" prop="appealResult">
            <el-radio-group v-model="form.appealResult" @change="getResult"
                            :disabled="seeDetail==true">
              <el-radio :label="'1'">通过</el-radio>
              <el-radio :label="'2'">不通过</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="评语" prop="comment">
            <el-input v-model="form.comment" placeholder="请输入评语" type="textarea" :disabled="seeDetail==true"/>
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cancel" v-if="seeDetail==true">关闭</el-button>
        <el-button type="primary" @click="submitForm"  v-if="seeDetail==false">确 定</el-button>
        <el-button @click="cancel"  v-if="seeDetail==false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listInfo, getDetail, checkAppeal} from "@/api/scholarship/appealInfo";
  import {getAtLeast} from "@/api/scholarship/timeSetting"
  import {getInfo} from "@/api/scholarship/instrorInfo"

  export default {
    name: "appealInfo",
    dicts: ['process', 'valid_term', 'check_result'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 成绩申诉表格数据
        appealInfoList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        //详情查看
        seeDetail: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          appealYear: null,
          appealTerm: null,
          appealStatus: null,
        },
        //专业id
        guideMajorIds:undefined,
        //年级
        grade: undefined,
        //专业下拉类别
        majorOptions: [],
        // 表单参数
        form: {},
        //时间参数信息
        timeInfo: {},
        // 表单校验
        rules: {
          comment:[{require:true,message:'评语不能为空',trigger:'blur'}]
        },
        instructorName:undefined,
      };
    },
    created() {
      this.getInstructorInfo();
    },
    methods: {
      //获取最新的时间参数信息
      getTimeSetting() {
        getAtLeast("1").then(res => {
          this.timeInfo = res.data;
          this.queryParams.appealYear = res.data.setYear;
          this.queryParams.appealTerm = res.data.setTerm;
          this.getList();
        })
      },

      //获取辅导员信息,并返回专业和年级信息
      getInstructorInfo() {
        getInfo().then(res => {
          if (res.data != undefined) {
            this.instructorName=res.data.instructorName
            let majorName = res.majorNames;
            //指导专业名称数组
            let majorIds = res.data.guideMajorIds.split(",")
            this.guideMajorIds=res.data.guideMajorIds
            this.grade = res.data.guideGrade
            //默认第一是值，第二个是索引
            majorName.forEach((item, index) => {
              let obj = {
                id: majorIds[index],
                label: item
              };
              //初始化专业的下拉项
              this.majorOptions.push(obj);
            });
          }
          this.getTimeSetting();
        })
      },

      addParmas(params) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        search.params['majorIds'] = this.guideMajorIds;
        search.params['grade'] = this.grade;
        search.params['majorName'] = this.queryParams.majorName;
        return search;
      },

      /** 查询成绩申诉列表 */
      getList() {
        this.loading = true;
        listInfo(this.addParmas(this.queryParams)).then(response => {
          this.appealInfoList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },

      //审核结果变化
      getResult(val) {
        if (val == '1') {
          this.form.comment = '同意'
        }
        else {
          this.form.comment = undefined;
        }

      },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          appealId: null,
          sno: null,
          stuName: null,
          appealYear: null,
          appealTerm: null,
          appealReason: null,
          appealStatus: "0",
          appealResult: null,
          comment: null
        };
        this.resetForm("form");
      },

      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.appealTerm = this.timeInfo.setTerm;
        this.queryParams.appealYear = this.timeInfo.setYear;
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.appealId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加成绩申诉";
      },

      muplitCheck(row) {
        //批量审核
        if (this.ids == undefined || this.ids.length == 0) {
          this.$modal.alertError("请先选择要审核的记录！")
        }
        else if (this.ids.length != 1) {
          //打开批量审核表单
          this.reset()
          this.single = false
          this.form.appealResult = '1'
          this.form.comment='同意'
          this.open = true
          this.title = '批量审核学生申请'
        }
        else {
          //单个审核
          this.handleCheck(row)
        }
      },

      /** 审核按钮操作 */
      handleCheck(row) {
        this.reset();
        const appealId = row.appealId
        getDetail(appealId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "审核成绩申诉";
          //默认审核通过
          this.form.appealResult = '1'
          this.form.comment = '同意'
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            this.form.checkPerson = this.instructorName
            //添加id
            let applyIds = []
            if (this.ids.length != 0) {
              applyIds = this.ids;
            }
            if (this.form.appealId != undefined) {
              applyIds.push(this.form.appealId)
            }
            this.form.params = typeof (this.form.params) === 'object' && this.form.params !== null && !Array.isArray(this.form.params) ? this.form.params : {};
            this.form.params['appealIds'] = applyIds
            checkAppeal(this.form).then(response => {
              this.$modal.msgSuccess("审核成功");
              this.open = false;
              this.getList();
            });
          }
        });
      },
      //查看
      handleDetail(row) {
        getDetail(row.appealId).then(response => {
          this.seeDetail=true
          this.form = response.data;
          this.open = true;
          this.title = "查看成绩申诉";
        });
      }
    }
  };
</script>
