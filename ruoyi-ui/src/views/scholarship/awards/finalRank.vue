<template xmlns:el-col="http://www.w3.org/1999/html">
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="评定学年" prop="yearInfo">
            <el-select v-model="queryParams.yearInfo" placeholder="请选择评定学年" clearable>
              <el-option
                v-for="dict in yearsOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="评定学期" prop="termInfo">
            <el-select v-model="queryParams.termInfo" placeholder="请选择" clearable>
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
          <el-form-item label="专业名称" prop="majorName" v-if="JSON.stringify(stu)=='{}'">
            <el-select v-model="queryParams.majorName" placeholder="请选择专业名称" clearable
            >
              <el-option
                v-for="dict in majorOptions"
                :key="dict.collegeId"
                :label="dict.name"
                :value="dict.name"
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
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="initList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="applyId" v-if="false" width="20"></el-table-column>
          <el-table-column label="学号" align="center" key="sno" prop="sno" v-if="columns[0].visible" width="100"/>
          <el-table-column label="姓名" align="center" key="stuName" prop="stuName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="100"/>
          <el-table-column label="专业" sortable align="center" key="majorName" v-if="columns[2].visible" width="150"
                           prop="majorName">
          </el-table-column>
          <el-table-column label="年级" sortable align="center" key="grade" v-if="columns[2].visible" width="100"
                           prop="grade">
          </el-table-column>
          <el-table-column label="评定学年" align="center" key="yearInfo" v-if="columns[2].visible" width="100"
                           prop="yearInfo">
          </el-table-column>
          <el-table-column label="评定学期" align="center" key="termInfo" prop="termInfo"
                           v-if="columns[3].visible" :show-overflow-tooltip="true" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.valid_term" :value="scope.row.termInfo"/>
            </template>
          </el-table-column>
          <el-table-column label="成绩绩点" align="center" key="total" prop="gradeCredit"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="120">
          </el-table-column>
          <el-table-column label="成绩排名" align="center" key="gradeRank" prop="gradeRank"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="110">
          </el-table-column>
          <el-table-column label="奖项加分" align="center" key="allExtraPoint" prop="allExtraPoint"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="120">
            <template slot-scope="scope">
              <span v-if="scope.row.allExtraPoint == null">0</span>
              <span v-if="scope.row.allExtraPoint != null">{{scope.row.allExtraPoint}}</span>
            </template>
          </el-table-column>
          <el-table-column label="最终绩点" align="center" sortable key="finalCredit" prop="finalCredit"
                           v-if="columns[5].visible " :show-overflow-tooltip="true" width="100">
          </el-table-column>
          <el-table-column label="最终排名" align="center" sortable key="finalRank" prop="finalRank"
                           v-if="columns[5].visible " :show-overflow-tooltip="true" width="100">
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
  </div>
</template>

<script>
  import Cookies from "js-cookie";
  import {getInfo} from "@/api/scholarship/instrorInfo"
  import {getInfoBysno, selClassByMajorIds} from "@/api/scholarship/stuInfo"
  import {getAllMajor, selByParentId} from "@/api/system/dept";
  import {listInitAward,updateIsPublic} from "@/api/scholarship/initAwardList"

  export default {
    name: "finalRank",
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
        initList: null,
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
          yearInfo: undefined,//申请学年
          termInfo: undefined,//申请学期
          majorName: undefined,
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
        // rules: {
        //   cardNum: [
        //     {required: true, message: "卡号不能为空", trigger: ["blur"]},
        //   ],
        //   selfRemark: [
        //     {required: true, message: "自我评价不能为空", trigger: ["blur"]},
        //   ],
        //
        // }
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
          selByParentId(100).then(res => {
            this.collegeOptions = res.data;
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
      // 学生登录:stu,
      // 辅导员：instructor
      addParams(params) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        search.params['collegeId'] = this.queryParams.collegeId;
        if (JSON.stringify(this.stu) != '{}') {
          search.params['grade'] = this.stu.grade;
          search.params['majorId'] = this.stu.majorId;
          search.params['stuClass'] = this.stu.stuClass;
        }
        if (JSON.stringify(this.instructor) != '{}') {
          search.params['guideGrade'] = this.instructor.guideGrade;
          search.params['majorIds'] = this.instructor.guideMajorIds;

        }
        return search;

      },
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listInitAward(this.addParams(this.queryParams)).then(response => {
            this.initList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },

      //学院下拉框
      getCollege(val) {
        this.queryParams.majorId = undefined;
        if (val != null && val != "") {
          selByParentId(val).then(res => {
            this.majorOptions = res.data;
          });
        }
      },

      // //发布初始名单
      // //修改可见名单的状态
      // publishList() {
      //   if (this.queryParams.yearInfo == undefined || this.queryParams.termInfo == undefined) {
      //     this.$modal.msgError("请先选择要发布的学年和学期信息!");
      //   }
      //   else {
      //     //获取学期信息
      //     let term = null;
      //     this.getDicts('valid_term').then((response) => {
      //       response.data.forEach((item => {
      //             if (this.queryParams.termInfo === item.dictValue) {
      //               term = item.dictLabel
      //             }
      //           }
      //         )
      //       );
      //       this.$modal.confirm('是否确认发布' + this.queryParams.yearInfo + '学年' + term + '的初始名单？').then(() => {
      //         //修改发布状态
      //         updateIsPublic(this.queryParams.yearInfo,this.queryParams.termInfo).then(res=>{
      //           this.$modal.msgSuccess("发布成功");
      //         })
      //
      //       })
      //     })
      //   }
      // }
      // ,

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
      this.form.cardNum = undefined;
      this.form.selfRemark = undefined;
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
  }
  ;
</script>
