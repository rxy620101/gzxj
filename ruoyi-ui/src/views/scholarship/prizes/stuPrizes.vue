<template xmlns:el-col="http://www.w3.org/1999/html">
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="学生姓名" prop="stuName" v-if="JSON.stringify(stu)=='{}'">
            <el-input
              v-model="queryParams.stuName"
              placeholder="请输入学生姓名"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="奖项名称" prop="prizeName">
            <el-input
              v-model="queryParams.prizeName"
              placeholder="请输入奖项名称"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="获奖学年" prop="getYear">
            <el-select v-model="queryParams.getYear" placeholder="请选择获奖学年" clearable>
              <el-option
                v-for="dict in yearsOOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="获奖学期" prop="getTerm">
            <el-select v-model="queryParams.getTerm" placeholder="请选择" clearable>
              <el-option
                v-for="dict in dict.type.valid_term"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="审核结果" prop="process"  v-if="JSON.stringify(stu)!='{}'">
            <el-select v-model="queryParams.process" placeholder="请选择" clearable>
              <el-option
                v-for="dict in dict.type.process"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="奖项类别" prop="parentId">
            <el-select v-model="queryParams.parentId" placeholder="请选择奖项类别"
                       @change="getType($event,'queryParams')" clearable>
              <el-option
                v-for="dict in prizeTypeOptions"
                :key="dict.prizeId"
                :label="dict.prizeType"
                :value="dict.prizeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="对应奖项" prop="prizeId">
            <el-select v-model="queryParams.prizeId" placeholder="请选择" clearable>
              <el-option
                v-for="dict in prizeNameOption"
                :key="dict.prizeId"
                :label="dict.prizeType"
                :value="dict.prizeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="专业" prop="majorId" v-if="JSON.stringify(stu)=='{}'">
            <el-select v-model="queryParams.majorId" placeholder="请选择专业" clearable @change="getMajor">
              <el-option
                v-for="dict in majorOptions"
                :key="dict.id"
                :label="dict.label"
                :value="dict.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="班级" prop="stuClass" v-if="JSON.stringify(stu)=='{}'">
            <el-select v-model="queryParams.stuClass" placeholder="请选择班级" clearable>
              <el-option
                v-for="dict in classOptions"
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
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['prizes:info:add']"
              v-if="JSON.stringify(stu) != '{}'"
            >登记
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['prizes:info:check']"
            >审核
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['prizes:info:remove']"
              v-if="JSON.stringify(stu) != '{}'"
            >删除
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
          <el-table-column label="id" prop="awardId" v-if="false" width="20"></el-table-column>
          <el-table-column label="学号" align="center" key="sno" prop="sno" v-if="columns[0].visible" width="100"/>
          <el-table-column label="姓名" align="center" key="stuName" prop="stuName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="100"/>
          <el-table-column label="获奖学年" sortable align="center" key="sex" v-if="columns[2].visible" width="160"
                           prop="getYear">
          </el-table-column>
          <el-table-column label="获奖学期" align="center" sortable key="getTerm" prop="getTerm"
                           v-if="columns[3].visible" :show-overflow-tooltip="true" width="150">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.valid_term" :value="scope.row.getTerm"/>
            </template>
          </el-table-column>
          <el-table-column label="奖项名称" align="center" key="prizeName" prop="prizeName"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="160">
          </el-table-column>
          <el-table-column label="对应分数" align="center" sortable key="getPoint" prop="getPoint"
                           v-if="columns[5].visible && form.process !='0'" :show-overflow-tooltip="true" width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.process != '0'">{{scope.row.getPoint}}</span>
              <span v-if="scope.row.process == '0'">待审核</span>
            </template>
          </el-table-column>
          <el-table-column label="审核进度" align="center" key="process" prop="process"
                           v-if="columns[6].visible" :show-overflow-tooltip="true" width="160">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.process" :value="scope.row.process"/>
            </template>
          </el-table-column>
          <el-table-column label="登记时间" align="center" prop="createTime" v-if="columns[7].visible"
                           :show-overflow-tooltip="true" width="130">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column width="160" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['prizes:info:edit']"
                v-if="JSON.stringify(stu) != '{}'"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['prizes:info:remove']"
                v-if="JSON.stringify(stu) != '{}'"
              >删除
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleCheck(scope.row)"
                v-hasPermi="['prizes:info:check']"
                v-if="userName == 'admin'"
              >审核
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
      </el-col>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="学号" prop="sno"  v-if="JSON.stringify(stu)=='{}'" >
              <el-input v-model="form.sno" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="stuName" v-if="JSON.stringify(stu)=='{}'" >
              <el-input v-model="form.stuName" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="获奖学年" prop="getYear">
              <el-select v-model="form.getYear" placeholder="请选择获奖学年" :disabled="JSON.stringify(stu)=='{}'">
                <el-option
                  v-for="dict in yearsOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="获奖学期" prop="getTerm">
              <el-select v-model="form.getTerm" placeholder="请选择获奖学期" :disabled="JSON.stringify(stu)=='{}'">
                <el-option
                  v-for="dict in dict.type.valid_term"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="奖项类别" prop="parentId">
              <el-select v-model="form.parentId" clearable placeholder="请选择奖项类别" @change="getType($event,'form')"
                         :disabled="JSON.stringify(stu)=='{}'">
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
            <el-form-item label="对应奖项" prop="prizeId">
              <el-select v-model="form.prizeId" placeholder="请选择对应奖项" clearable
                         :disabled="JSON.stringify(stu)=='{}'">
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
          <el-col :span="24">
            <el-form-item label="奖项名称" prop="prizeName">
              <el-input v-model="form.prizeName" placeholder="请输入奖项名称" clearable
                        :disabled="JSON.stringify(stu)=='{}'"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!--奖项图片-->
        <el-row>
        <el-col>
        <el-form-item label="奖项图片" prop="prizeImg">
          <ImgUpload
            v-model="form.prizeImg"
            :limit="1"
            :disabled="JSON.stringify(stu)=='{}'"
          >
          </ImgUpload>
        </el-form-item>
        </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"
                        :disabled="JSON.stringify(stu)=='{}'"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="对应分数" prop="getPoint" v-if="userName === 'admin'">
              <el-input-number v-model="form.getPoint" placeholder="对应分数" :min="0" :step="0.1"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核" prop="process" v-if="userName ==='admin'">
              <el-radio-group v-model="form.process" @change="getProcess">
                <el-radio :label="'1'">通过</el-radio>
                <el-radio :label="'2'">不通过</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="评语" v-if="userName === 'admin'" prop="comment">
              <el-input v-model="form.comment" type="textarea" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

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
  import {listPrizes, getPrizes, delPrizes, addPrizes, updatePrizes, updateProcess} from "@/api/scholarship/stuPrizes";
  import {getPrizetype, selByParentId} from "@/api/scholarship/prizeType";
  import {getAllMajor} from "@/api/system/dept";
  import ImgUpload from "@/views/scholarship/prizes/imgUpload/imgUpload"

  export default {
    name: "stuPrizes",
    components:{ImgUpload},
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
        classOptions: [],
        majorIds: [],
        majorOptions: [],
        yearsOptions: [
          {id: 1, value: '2017-2018'},
          {id: 2, value: '2018-2019'},
          {id: 3, value: '2019-2020'},
          {id: 4, value: '2020-2021'},
          {id: 5, value: '2021-2022'},
          {id: 6, value: '2022-2023'},
        ],

        // 表单参数
        form: {
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          //学生角度
          sno: undefined,
          parentId: undefined,
          prizeId: undefined,
          getYear: undefined,//申请学年
          getTerm: undefined,//申请学期
          process: undefined,//审核进度
          //辅导员
          majorId: undefined,
          stuClass: undefined,
          stuName: undefined,
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
          getYear: [{required: true, message: "获奖学年不能为空", trigger: ["blur", "change"]},],
          getTerm: [{required: true, message: "获奖学期不能为空", trigger: ["blur", "change"]},],
          parentId: [
            {required: true, message: "奖项类别不能为空", trigger: ["blur", "change"]},
          ],
          prizeId: [
            {required: true, message: "对应奖项不能为空", trigger: ["blur", "change"]},
          ],
          prizeName: [
            {required: true, message: "奖项名称不能为空", trigger: ["blur"]},
          ],
          prizeImg: [
            {required: true, message: "奖项图片不能为空", trigger: ["blur"]},
          ],

          // 默认审核通过 1
          comment: [{
            //管理员审核
            validator: (rule, value, callback) => {
              if (this.userName != 'admin') {
                callback()
              }
              //通过时评语默认为通过
                else{
                  if (value == undefined || JSON.stringify(value) =='') {
                    callback("评语不能为空")
                  }
                  else{
                    callback()
                  }
                }
            },
            trigger: "blur"
          }],
          getPoint: [{
            //管理员审核
            validator: (rule, value, callback) => {
              if (this.userName != 'admin') {
                callback()
              }
              else {
                //审核通过，则分数不能为0 this.form.process == '1' &&
                if ( value == 0) {
                  callback("审核通过则对应分数不能为0!")
                }
                else{
                  callback()
                }
              }

            },
            trigger: "blur"
          }],
        }
      };
    },
    watch: {
    }
    ,
    created() {
      this.getInstructorInfo();
      //初始化奖项类型数组
      this.getType("1")
    }
    ,
    methods: {
      //获取辅导员信息,并返回专业和年级信息
      getInstructorInfo() {
        this.userName = Cookies.get("username");
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
          console.log(this.stu)
          this.getList()

        })
      },
      //添加参数
      // 学生登录:sno,
      // 辅导员：guideGrade、majorIds,majorId,stuClass,stuName,process
      addParams(params) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        if (JSON.stringify(this.stu)!= '{}') {
          search.params['sno'] = this.stu.sno;
        }
        if (JSON.stringify(this.instructor)!= '{}') {
          search.params['majorIds'] = this.majorIds.join(",");
          search.params['grade'] = this.instructor.guideGrade;
          search.params['majorId'] = this.queryParams.majorId;
          search.params['stuClass'] = this.queryParams.stuClass;
          search.params['stuName'] = this.queryParams.stuName;
          search.params['process'] = '1';
        }
        if(this.userName=='admin'){
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
      //班级下拉初始化
      getMajor(val) {
        this.queryParams.majorId = val;
        this.queryParams.stuClass = undefined;
        if (val != '') {
          let className = []
          selClassByMajorIds(val).then(res => {
            let classArray = res.data;
            classArray.forEach((item, index) => {
              className.push({
                id: index,
                value: item,
              })
            })
            this.classOptions = className
          })
        }
        else {
          this.classOptions = []
        }
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      }
      ,
      //审核状态切换
      getProcess(val){
        if(val =='2'){
          this.form.comment=undefined;
        }
        if(val=='1'){
          this.form.comment='审核通过'
        }
      },
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
        this.ids = selection.map(item => item.awardId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "学生所获奖项登记";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        //重新执行专业、年级的下拉框选项
        // this.getCollege(row.collegeId, 'form')
        const stuId = row.awardId || this.ids;
        getPrizes(stuId).then(response => {
          let data = response.data;
          this.form = data;
          this.form.parentId=parseInt(this.form.parentId);
          this.form.prizeId=parseInt(this.form.prizeId);
          //重新获取下拉项
          this.getType(this.form.parentId)
          this.open = true;
          if(this.userName == 'admin'){
            this.title = "修改学生所获奖项";
          }
          else{
            this.title="审核学生所获奖项"
          }
          //获取分数信息
          if(this.userName == 'admin'){
            this.form.process="1";
            this.form.comment='审核通过'
            getPrizetype(this.form.prizeId).then(res=>{
              if(res.data.typeName == this.form.prizeName){
                this.form.getPoint=res.data.extraPoint;
              }
            })
          }
        });
      }
      ,
      //审核按钮
      handleCheck(row){
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
              this.form.sno=this.userName;
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
