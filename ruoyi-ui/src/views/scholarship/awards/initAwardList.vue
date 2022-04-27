<template xmlns:el-col="http://www.w3.org/1999/html">
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="评定学年" prop="applyYear">
            <el-select v-model="queryParams.applyYear" placeholder="请选择评定学年" clearable>
              <el-option
                v-for="dict in yearsOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="评定学期" prop="applyTerm">
            <el-select v-model="queryParams.applyTerm" placeholder="请选择" clearable>
              <el-option
                v-for="dict in dict.type.valid_term"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学院名称" prop="collegeId" v-if="JSON.stringify(stu)=='{}'">
            <el-select v-model="queryParams.collegeId" placeholder="请选择学院名称" clearable
                       @change="getCollege($event,'queryParams')">
              <el-option
                v-for="dict in collegeOptions"
                :key="dict.collegeId"
                :label="dict.name"
                :value="dict.collegeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="专业名称" prop="majorId" v-if="JSON.stringify(stu)=='{}'">
            <el-select v-model="queryParams.majorId" placeholder="请选择专业名称" clearable
            >
              <el-option
                v-for="dict in majorOptions"
                :key="dict.collegeId"
                :label="dict.name"
                :value="dict.collegeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="年级" prop="grade" v-if="userName == 'admin'">
            <el-select v-model="queryParams.grade" placeholder="请选择年级" clearable>
              <el-option
                v-for="dict in gradeOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
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
              type="primary"
              plain
              icon="el-icon-guide"
              size="mini"
              @click="getInit"
              v-if="userName == 'admin'"
            >生成初始名单
            </el-button>
            <el-button
              type="primary"
              plain
              icon="el-icon-s-promotion"
              size="mini"
              @click="publishList"
              v-if="userName == 'admin'"
            >发布初始名单
            </el-button>
          </el-col>
          <!--</el-col>-->
          <!--<el-col :span="1.5">-->
          <!--<el-button-->
          <!--type="warning"-->
          <!--plain-->
          <!--icon="el-icon-download"-->
          <!--size="mini"-->
          <!--@click="handleExport"-->
          <!--v-hasPermi="['stu:info:export']"-->
          <!--&gt;导出-->
          <!--</el-button>-->
          <!--</el-col>-->
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="prizesList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="applyId" v-if="false" width="20"></el-table-column>
          <el-table-column label="学号" align="center" key="sno" prop="sno" v-if="columns[0].visible" width="100"/>
          <el-table-column label="姓名" align="center" key="stuName" prop="stuName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="100"/>
          <el-table-column label="评定学年" sortable align="center" key="applyYear" v-if="columns[2].visible" width="160"
                           prop="applyYear">
          </el-table-column>
          <el-table-column label="评定学期" align="center" sortable key="applyTerm" prop="applyTerm"
                           v-if="columns[3].visible" :show-overflow-tooltip="true" width="150">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.valid_term" :value="scope.row.getTerm"/>
            </template>
          </el-table-column>
          <el-table-column label="奖项名称" align="center" key="awardName" prop="awardName"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="160">
          </el-table-column>
          <el-table-column label="奖项初始等级" align="center" sortable key="rankName" prop="rankName"
                           v-if="columns[5].visible " :show-overflow-tooltip="true" width="100">
          </el-table-column>
          <el-table-column label="申请时间" align="center" prop="createTime" v-if="columns[7].visible"
                           :show-overflow-tooltip="true" width="130">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="审核进度" align="center" key="process" prop="process"
                           v-if="columns[6].visible" :show-overflow-tooltip="true" width="160">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.process" :value="scope.row.process"/>
            </template>
          </el-table-column>
          <el-table-column width="160" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['award:info:query']"
              >查看
              </el-button>
              <!--<el-button-->
              <!--size="mini"-->
              <!--type="text"-->
              <!--icon="el-icon-delete"-->
              <!--@click="handleDelete(scope.row)"-->
              <!--v-hasPermi="['prizes:info:remove']"-->
              <!--v-if="JSON.stringify(stu) != '{}'"-->
              <!--&gt;删除-->
              <!--</el-button>-->
              <!--<el-button-->
              <!--size="mini"-->
              <!--type="text"-->
              <!--icon="el-icon-edit"-->
              <!--@click="handleCheck(scope.row)"-->
              <!--v-hasPermi="['prizes:info:check']"-->
              <!--v-if="userName == 'admin'"-->
              <!--&gt;审核-->
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
              <el-input v-model="form.sno" disabled>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="stuName">
              <el-input v-model="form.stuName" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请学年" prop="applyYear">
              <el-input v-model="form.applyYear" disabled>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请学期" prop="applyTerm">
              <el-input v-model="form.applyTerm" disabled>
                <template slot-scope="scope">
                  <span v-if="form.appleyTerm == '1'">第一学期</span>
                  <span v-if="form.appleyTerm == '2'">第二学期</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="奖项名称" prop="applyType">
              <el-select v-model="form.applyTpe" clearable
                         disabled>
                <el-option
                  v-for="dict in prizeTypeOptions"
                  :key="dict.prizeId"
                  :label="dict.prizeType"
                  :value="dict.prizeId"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="奖项等级" prop="initRank">
              <el-select v-model="form.initRank"
                         disabled>
                <el-option
                  v-for="dict in prizeNameOption"
                  :key="dict.prizeId"
                  :label="dict.prizeType"
                  :value="dict.prizeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="奖项金额" prop="money">
              <el-input v-model="form.money"
                        disabled
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卡号" prop="cardNum">
              <el-input v-model="form.cardNum"
                        :disabled="JSON.stringify(stu)=='{}'"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="自我评价" prop="selfRemark">
              <el-input v-model="form.selfRemark" type="textarea" placeholder="请输入内容"
                        :disabled="JSON.stringify(stu)=='{}'"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!--<el-row>-->
        <!--<el-col :span="12">-->
        <!--<el-form-item label="审核" prop="process" v-if="userName ==='admin'">-->
        <!--<el-radio-group v-model="form.process" @change="getProcess">-->
        <!--<el-radio :label="'1'">通过</el-radio>-->
        <!--<el-radio :label="'2'">不通过</el-radio>-->
        <!--</el-radio-group>-->
        <!--</el-form-item>-->
        <!--</el-col>-->
        <!--</el-row>-->
        <!--<el-row>-->
        <!--<el-col :span="24">-->
        <!--<el-form-item label="评语" v-if="userName === 'admin'" prop="comment">-->
        <!--<el-input v-model="form.comment" type="textarea" placeholder="请输入内容"></el-input>-->
        <!--</el-form-item>-->
        <!--</el-col>-->
        <!--</el-row>-->

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Cookies from "js-cookie";
  import {getInfo} from "@/api/scholarship/instrorInfo"
  import {getInfoBysno, selClassByMajorIds} from "@/api/scholarship/stuInfo"
  import {getPrizetype, selByParentId} from "@/api/scholarship/prizeType";
  import {getAllMajor} from "@/api/system/dept";
  import {listInitAward}from "@/api/scholarship/initAwardList"

  export default {
    name: "initAwardList",
    dicts: ['process', 'valid_term'],
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
        prizesList: null,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        prizeTypeOptions: [],
        prizeNameOption: [],
        stu: {},
        instructor: {},
        //辅导员登录时
        majorIds: [],
        //管理员登录
        collegeOptions: [],
        majorOptions: [],
        gradeOptions: [
          {id: 1, value: 2017},
          {id: 2, value: 2018},
          {id: 3, value: 2019},
          {id: 4, value: 2020},
          {id: 5, value: 2021},
          {id: 6, value: 2022},
        ],
        yearsOptions: [
          {id: 1, value: '2017-2018'},
          {id: 2, value: '2018-2019'},
          {id: 3, value: '2019-2020'},
          {id: 4, value: '2020-2021'},
          {id: 5, value: '2021-2022'},
          {id: 6, value: '2022-2023'},
        ],

        // 表单参数
        form: {},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          //学生角度
          sno: undefined,
          applyYear: undefined,//申请学年
          applyTerm: undefined,//申请学期
          //辅导员
          majorId: undefined,
          //管理员
          collegeId: undefined,
          grade: undefined,
        },
        //当前用户
        userName: undefined,
        // 列信息
        columns: [
          {key: 0, label: `学号`, visible: true},
          {key: 1, label: `姓名`, visible: true},
          {key: 2, label: `获奖学年`, visible: true},
          {key: 3, label: `获奖学期`, visible: true},
          {key: 4, label: `奖项名称`, visible: true},
          {key: 5, label: `对应分数`, visible: true},
          {key: 6, label: `审核进度`, visible: true},
          {key: 7, label: `登记时间`, visible: true},
        ],
        // 表单校验
        rules: {
          cardNum: [
            {required: true, message: "卡号不能为空", trigger: ["blur"]},
          ],
          selfRemark: [
            {required: true, message: "自我评价不能为空", trigger: ["blur"]},
          ],

        }
      }
    },

    created() {

      //获取辅导员信息，确定是哪个登录者
      this.getLoginInfo()
      //初始化奖项类型数组
      this.getType("1")
    }
    ,
    methods: {
      //确定是哪个登录者
      getLoginInfo() {
        this.userName = Cookies.get("username");
        if (this.userName == 'admin') {
          //获取学院和专业的下拉项
          //获取学院 100是顶级
          this.getByParentId(100).then(res => {
            this.collegeOptions = res;
            this.getList()
          });
        }
        else {
          this.getInstructorInfo();
        }
      },
      //获取辅导员信息,并返回专业和年级信息
      getInstructorInfo() {
        if (this.userName != 'admin') {
          getInfo().then(res => {
            if (res.data != undefined) {
              this.instructor = res.data;
              let majorName = res.majorNames;
              //指导专业名称数组
              this.majorIds = res.data.guideMajorIds.split(",")
              //默认第一是值，第二个是索引
              majorName.forEach((item, index) => {
                let obj = {
                  id: this.majorIds[index],
                  label: item
                };
                //初始化专业的下拉项
                this.majorOptions.push(obj);
              });
            }
            this.getStuInfo();
          })
        }
        else {
          this.getList();
          //查询所有的专业
          getAllMajor().then(res => {
            res.data.forEach(item => {
              this.majorOptions.push({
                id: item.collegeId,
                label: item.name,
              })
            })

          })

        }
      },
      //获取学生信息
      getStuInfo() {
        getInfoBysno(Cookies.get("username")).then(res => {
          if (res.data != undefined) {
            this.stu = res.data;
          }
          this.getList()

        })
      },

      //添加参数
      // 学生登录:sno,
      // 辅导员：guideGrade、majorIds,majorId,stuClass,stuName,process
      addParams(params) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        if (JSON.stringify(this.stu) != '{}') {
          search.params['sno'] = this.stu.sno;
        }
        if (JSON.stringify(this.instructor) != '{}') {
          search.params['majorIds'] = this.majorIds.join(",");
          search.params['grade'] = this.instructor.guideGrade;
          search.params['majorId'] = this.queryParams.majorId;
          search.params['stuClass'] = this.queryParams.stuClass;
          search.params['stuName'] = this.queryParams.stuName;
          search.params['process'] = '1';
        }
        if (this.userName == 'admin') {
          search.params['process'] = '0';
        }
        return search;
      },
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listPrizes(this.addParams(this.queryParams)).then(response => {
            this.prizesList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },

      //学院下拉框
      getCollege(val) {
        this.queryParams.majorId = undefined;
        if (val != null && val != "") {
          this.getByParentId(val).then(res => {
            this.majorOptions = res;
          });
        }
      },

      //生成初始名单
      getInit(){

      },

      //发布初始名单
      publishList(){

      },

      //奖项类别初始化
      getType(val, reset) {
        if (reset == 'form') {
          this.form.prizeId = undefined;
          this.prizeNameOption = []
        }
        else {
          this.queryParams.prizeId = undefined;
          this.prizeNameOption = []
        }
        if (val != '') {
          selByParentId(val).then(res => {
            let dataList = res.data;
            let dataArray = []
            dataList.forEach(item => {
              dataArray.push({
                prizeId: item.typeId,
                prizeType: item.typeName
              })
            })
            if (val == "1") {
              this.prizeTypeOptions = dataArray;
            }
            else {
              this.prizeNameOption = dataArray;
            }
          })
        }
      },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      }
      ,
      // //审核状态切换
      // getProcess(val) {
      //   if (val == '2') {
      //     this.form.comment = undefined;
      //   }
      //   if (val == '1') {
      //     this.form.comment = '审核通过'
      //   }
      // },
      // 表单重置
      reset() {
        this.form = {
          sno: undefined,
          stuName: undefined,
          parentId: undefined,
          prizeId: undefined,
          prizeName: undefined,
          getPoint: undefined,
          remark: undefined,
          process: '0',
        };
        // this.resetForm("form");
        this.form.cardNum=undefined;
        this.form.selfRemark=undefined;
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
        this.ids = selection.map(item => item.applyId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "学生奖学金申请";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        //重新执行专业、年级的下拉框选项
        // this.getCollege(row.collegeId, 'form')Id
        const stuId = row.applyId || this.ids;
        getApply(stuId).then(response => {
          let data = response.data;
          this.form = data;
          this.form.parentId = parseInt(this.form.parentId);
          this.form.prizeId = parseInt(this.form.prizeId);
          //重新获取下拉项
          this.getType(this.form.parentId)
          this.open = true;
          if (this.userName == 'admin') {
            this.title = "修改学生所获奖项";
          }
          else {
            this.title = "审核学生所获奖项"
          }
          //获取分数信息
          if (this.userName == 'admin') {
            this.form.process = "1";
            this.form.comment = '审核通过'
            getPrizetype(this.form.prizeId).then(res => {
              if (res.data.typeName == this.form.prizeName) {
                this.form.getPoint = res.data.extraPoint;
              }
            })
          }
        });
      }
      ,
      //审核按钮
      handleCheck(row) {
        //获取学生申请信息
        this.handleUpdate(row);
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          console.log('yyyy')
          if (valid) {
            if (this.form.awardId != undefined) {
              updatePrizes(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              this.form.sno = this.userName;
              addPrizes(this.form).then(response => {
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
        const stuIds = row.awardId || this.ids;
        this.$modal.confirm('是否确认删除这些数据项？').then(function () {
          return delPrizes(stuIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
      ,
      // /** 导出按钮操作 */
      // handleExport() {
      //   this.download('scholarShip/stuInfo/export', {
      //     ...this.queryParams
      //   }, `stuInfo_${new Date().getTime()}.xlsx`)
      // }
    }
  };
</script>
