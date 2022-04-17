<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="专业名称" prop="majorName">
            <el-select v-model="queryParams.majorName" placeholder="请选择专业名称" clearable
            >
              <el-option
                v-for="dict in majorOptions"
                :key="dict.id"
                :label="dict.label"
                :value="dict.label"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学年" prop="startYear">
            <el-select v-model="queryParams.startYear" placeholder="请选择学年" clearable>
              <el-option
                v-for="dict in yearsOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学期" prop="validTerm">
            <el-select
              v-model="queryParams.validTerm"
              placeholder="请选择学期"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.valid_term"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学生姓名" prop="stuName">
            <el-input
              v-model="queryParams.stuName"
              placeholder="请输入学生姓名"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <!--<el-col :span="1.5">-->
          <!--<el-button-->
          <!--type="primary"-->
          <!--plain-->
          <!--icon="el-icon-plus"-->
          <!--size="mini"-->
          <!--@click="handleAdd"-->
          <!--v-hasPermi="['stu:info:add']"-->
          <!--&gt;新增-->
          <!--</el-button>-->
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['grade:info:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              v-hasPermi="['grade:info:import']"
            >导入
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['grade:info:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="gradeList" @selection-change="handleSelectionChange" :render-header="labelHead">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="id" v-if="false" width="20"></el-table-column>
          <el-table-column label="学号" align="center" key="sno" prop="sno" v-if="columns[0].visible" width="100"/>
          <el-table-column label="姓名" align="center" key="stuName" prop="stuName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="80"/>
          <el-table-column>

            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['grade:info:edit']"
              >修改
              </el-button>
              <!--<el-button-->
              <!--size="mini"-->
              <!--type="text"-->
              <!--icon="el-icon-delete"-->
              <!--@click="handleDelete(scope.row)"-->
              <!--v-hasPermi="['grade:info:remove']"-->
              <!--&gt;删除-->
              <!--</el-button>-->
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
      </el-col>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学号" prop="sno">
              <el-input v-model="form.sno" placeholder="请输入学号" :disabled="form.id != undefined"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport"/>
            是否更新已经存在的数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;"
                   @click="importTemplate">下载模板
          </el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {listInfo, getGradeInfo, delInfo, addInfo, updateInfo} from "@/api/scholarship/gradeInfo";
  import {getInfo} from "@/api/scholarship/instrorInfo"
  import {getInfoBysno} from "@/api/scholarship/stuInfo"
  import {getToken} from "@/utils/auth";
  import Cookies from "js-cookie";

  export default {
    name: "gradeImport",
    dicts: ['valid_term'],
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
        // 表格数据
        gradeList: null,
        //学科数据
        courseList: null,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        yearsOptions: [
          {id: 1, value: '2017-2018'},
          {id: 2, value: '2018-2019'},
          {id: 3, value: '2019-2020'},
          {id: 4, value: '2020-2021'},
          {id: 5, value: '2021-2022'},
          {id: 6, value: '2022-2023'},
        ],
        majorOptions: [],
        //动态列头header
        header: [],
        majorNames:null,
        stuClass:undefined,
        majorId:undefined,
        //分数列表
        scoreList:[],

        // 表单参数
        form: {
          id: undefined,
          stuName: undefined,
          collegeId: undefined,
          majorId: undefined,
          grade: undefined,
          stuClass: undefined,
        },
        // 用户导入参数
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 是否更新已经存在的用户数据
          updateSupport: 0,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/scholarShip/stuInfo/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          stuName: undefined,
          majorName: undefined,
          startYear: "2021-2022",
          validTerm: "1",
        },
        grade: undefined,
        // 列信息
        columns: [
          {key: 0, label: `学号`, visible: true},
          {key: 1, label: `姓名`, visible: true},
          {key: 2, label: `性别`, visible: true},
          {key: 3, label: `学院`, visible: true},
          {key: 4, label: `专业`, visible: true},
          {key: 5, label: `年级`, visible: true},
          {key: 6, label: `班级`, visible: true},
          {key: 7, label: `入学时间`, visible: true},
        ],
        // 表单校验
        rules: {},
      }
    },

    created() {
      // this.getList();
      this.getInstructorInfo();
      // this.getStuInfo();

    }
    ,
    methods: {
      //动态表头渲染
      labelHead(h,{column,index}){
        //let l = column.label.length;
        //let f = 12; //每个字大小,其实是每个字的比例值,大概会比字体大小打差不多大
        //column.minWidth = f * l; //字大小乘个数即长度 ,注意不要加px像素，这里minWidth只是一个比例值，不是真正的长度
        //然后将列标题放在一个div块中，注意块的宽度一定要100%，否则表格显示不完全
        return h('span',{class:'table-head',style:{width:'100%'}},[column.label])
      },
      //添加参数
      addParams(params,stu,startYear,majorName,validTerm) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        search.params['startYear'] = startYear;
        search.params['grade'] = this.grade;
        search.params['majorName'] = majorName;
        search.params['validTerm'] = validTerm;
        search.params['majorNames']=this.majorNames;
        search.params['stuClass']=this.stuClass;
        search.params['majorId']=this.majorId;
        return search;
      },

      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listInfo(this.addParams(this.queryParams,this.stu,this.queryParams.startYear,this.queryParams.majorName,this.queryParams.validTerm)).then(response => {
            this.gradeList = response.rows;
            //动态生成表头和对应的数据项
            this.gradeList[0].courseList.forEach(item=>{
              //分数列表
              this.scoreList.push(item.score);
              //表头

            })
            this.total = response.total;
            this.loading = false;
          }
        );

      },
      //获取辅导员信息,并返回专业和年级信息
      getInstructorInfo() {
        getInfo().then(res => {
          this.grade = res.data.guideGrade;
          let majorName = res.majorNames;
          this.majorNames=res.majorNames.join(",");
          //默认第一是值，第二个是索引
           majorName.forEach((item, index) => {
            let obj = {
              id: index,
              label: item
            };
            this.majorOptions.push(obj);
            //默认选中值为第一个
             this.queryParams.majorName=majorName[0]
          });
          this.getStuInfo();
        })
      },
      //获取学生信息
      getStuInfo(){
        getInfoBysno(Cookies.get("username")).then(res=>{
          if(res.data!=null){
            this.stuClass=res.data.stuClass;
            this.majorId=res.data.majorId;
          }
          this.getList()

        })
      }
      ,
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      }
      ,
      // 表单重置
      reset() {
        this.form = {
          sno: undefined,
          stuName: undefined,
          sex: "0",
          certNum: undefined,
          collegeId: undefined,
          majorId: undefined,
          grade: undefined,
          stuClass: undefined,
          remark: undefined,
        };
        this.resetForm("form");
      }
      ,
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      }
      ,
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      }
      ,
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.sno);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加学生基础信息";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        //重新执行专业、年级的下拉框选项
        this.getCollege(row.collegeId, 'form')
        const stuId = row.id || this.ids;
        getInfo(stuId).then(response => {
          let data = response.data;
          //将年级转为int
          data.grade = parseInt(data.grade)
          this.getGrade(data.grade, 'form')
          this.form = data
          this.open = true;
          this.title = "修改学生基础信息";
        });
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != undefined) {
              updateInfo(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addInfo(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      }
      ,
      /** 删除按钮操作 */
      handleDelete(row) {
        const stuIds = row.id || this.ids;
        this.$modal.confirm('是否确认删除学生编号为"' + stuIds + '"的数据项？').then(function () {
          return delInfo(stuIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
      ,
      /** 导出按钮操作 */
      handleExport() {
        this.download('scholarShip/stuInfo/export', {
          ...this.queryParams
        }, `stuInfo_${new Date().getTime()}.xlsx`)
      }
      ,
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "学生基础信息导入";
        this.upload.open = true;
      }
      ,
      /** 下载模板操作 */
      importTemplate() {
        this.download('scholarShip/stuInfo/importTemplate', {}, `stuInfo_template_${new Date().getTime()}.xlsx`)
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      }
      ,
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
        this.getList();
      }
      ,
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      }
    }
  };
</script>
