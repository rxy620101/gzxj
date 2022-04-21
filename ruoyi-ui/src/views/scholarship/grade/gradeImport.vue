<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="专业名称" prop="majorName" v-if="stu=null">
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
          <el-form-item label="学年" prop="startYear">
            <el-select v-model="queryParams.startYear" placeholder="请选择学年">
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

        <el-table
          v-loading="loading"
          :data="gradeList"
          @selection-change="handleSelectionChange"
        >
          <!--:default-sort = "{prop: 'totalCredit', order: 'descending'}"-->

          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column
            v-for="(item, index) in header"
            :key="'s'+ index"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"
            :show-overflow-tooltip="true"
            v-if="columns[index].visible"
            :sortable="item.sort !=null"
            align="center"
          >
          </el-table-column>
          <el-table-column
            width="100">
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
              <!--icon="el-icon-more"-->
              <!--@click="selectDetail(scope.row)"-->
              <!--v-hasPermi="['grade:info:detail']"-->
              <!--&gt;详情-->
              <!--</el-button>-->
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

    <!--&lt;!&ndash; 添加或修改对话框 &ndash;&gt;-->
    <!--<el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>-->
    <!--<el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
    <!--<el-row>-->
    <!--<el-col :span="12">-->
    <!--<el-form-item label="学号" prop="sno">-->
    <!--<el-input v-model="form.sno" placeholder="请输入学号" :disabled="form.id != undefined"/>-->
    <!--</el-form-item>-->
    <!--</el-col>-->
    <!--</el-row>-->
    <!--</el-form>-->
    <!--<div slot="footer" class="dialog-footer">-->
    <!--<el-button type="primary" @click="submitForm">确 定</el-button>-->
    <!--<el-button @click="cancel">取 消</el-button>-->
    <!--</div>-->
    <!--</el-dialog>-->

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
  import {
    listInfo,
    getGradeInfo,
    delInfo,
    addInfo,
    updateInfo,
    getHeader,
    getParams
  } from "@/api/scholarship/gradeInfo";
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
        gradeList: [],
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
        //科目数据
        subject: [],
        majorNames: null,
        //便于学生的过滤，学生只能查看本班级和本专业的
        stu:{
        },
        stuClass: undefined,
        majorId: undefined,

        // 表单参数
        // form: {
        //   id: undefined,
        //   stuName: undefined,
        //   collegeId: undefined,
        //   majorId: undefined,
        //   grade: undefined,
        //   stuClass: undefined,
        // },
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
          url: process.env.VUE_APP_BASE_API + "/grade/info/importData"
        },
        //初始的major值
        initMajor: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          stuName: undefined,
          //默认选中值为第一个
          majorName: undefined,
          startYear: "2021-2022",
          validTerm: "1",
          titleNames: [],
          grade:undefined,
        },
        //grade: undefined,
        // 列信息
        columns: [],
        // // 表单校验
        // rules: {},
        // //查看详情
        // selectDetial: false,
        //表头数据
        titleNames: []

      }
    },

    created() {
      this.getInstructorInfo();
    }
    ,
    methods: {
      //动态表头渲染
      /**
       * 根据学科建立表头
       * * 学号、姓名、【各个学科】、总分、平均分、名次
       */
      createTableHead() {
        // 添加学生
        let head = [
          {
            prop: 'sno',
            label: '学号',
            width: 110,
            align: 'center',

          },
          {
            prop: 'stuName',
            label: '姓名',
            width: 90
          }]

        // 添加科目
        for (let key in this.subject) {
          let sub = this.subject[key]
          head.push({
            prop: "sub_" + sub.id,
            label: sub.coseName,
          })
        }

        head.push({
          prop: 'totalScore',
          label: '总分',
          width: 80
        })
        head.push({
          prop: 'totalCredit',
          label: '总成绩绩点',
          width: 110,
          sort: true,
        })
        head.push({
          prop: 'ranking',
          label: '名次',
          width: 50
        })
        return head
      },
      // //动态适应表头宽度
      // headSpanFit (h, { column, index }) {
      //   let labelLong = column.label.length // 表头label长度
      //   let size = 20 // 根据需要定义标尺，直接使用字体大小确定就行，也可以根据需要定义
      //   column.minWidth = labelLong * size // 根据label长度计算该表头最终宽度
      //   return h('span', { class: 'cell-content', style: { width: '100%' } }, [column.label])
      // },

      //获取列信息
      getColumns() {
        this.header.forEach((item, index) => {
          let obj = {
            key: index,
            label: item.label,
            visible: true,
          }
          this.columns.push(obj)
        })

      },

      // //查看课程的详细信息
      // selectDetail() {
      //   this.selectDetial = ture;
      //
      // },

      //添加参数
      addParams(params, startYear, majorName, validTerm) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        search.params['startYear'] = startYear;
        search.params['grade'] = this.queryParams.grade;
        search.params['majorName'] = majorName;
        search.params['validTerm'] = validTerm;
        // search.params['majorNames'] = this.majorNames;
        search.params['stuClass'] = this.stuClass;
        search.params['majorId'] = this.majorId;
        return search;
      },

      /** 查询用户列表 */
      getList() {
        this.header = [];
        this.gradeList = [];
        this.subject = [];
        this.loading = true;
        listInfo(this.addParams(this.queryParams, this.queryParams.startYear, this.queryParams.majorName, this.queryParams.validTerm)).then(response => {
            let data = response.rows;
            //无课程信息的
            if (data.length == 0) {
              this.$modal.msgError("暂无课程信息");
            }
            else if (data[0].coseId != null) {
              //无学生成绩信息的，有课程的
              data.forEach((item, index) => {
                this.subject.push({
                  id: index,
                  coseId: item.coseId,
                  coseName: item.coseName,
                })
              })
              this.header = this.createTableHead();
              this.getColumns();
              console.log(this.column)
            }
            else {
              //有学生成绩信息的
              //动态生成表头
              //表头
              data[0].courseInfo.forEach((item, index) => {
                this.subject.push({
                  id: index,
                  coseId: item.coseId,
                  coseName: item.coseName,
                  // score:item.score,
                  credit: item.credit,
                  grade: item.grade,
                })
              })
              this.header = this.createTableHead();
              this.getColumns();
              //数据项 {  [] }
              data.forEach((val, index) => {
                val.courseInfo.forEach((item, num) => {
                  if (typeof this.gradeList[index] === 'undefined') {
                    // 没有记录。创建一行学生成绩，加入固定列的数据
                    this.gradeList[index] = {
                      sno: val.sno, // 学生ID
                      stuName: val.stuName, // 根据id获取学生姓名
                      totalScore: 0, // 学生的各科总分，后续修改
                      totalCredit:0,
                      ranking: 1 // 名次，后续修改
                    }
                  }
                  // 记录各科分数和学分
                  this.gradeList[index]['sub_' + num] = item.score
                  this.gradeList[index]['subCredit_' + num] = item.credit
                  this.gradeList[index]['getCredit_' + num] = item.haveCredit
                })
              })

              //计算总分和总学分
              this.getTotalScore();
              //计算排名
              this.getRank();
            }
            this.total = response.total;
            this.loading = false;
          }
        );

      },
      getTotalScore() {
        // 计算总分、总学分
        for (let key in this.gradeList) {
          let code = this.gradeList[key]
          // 遍历学科
          let total = 0
          let totalCredit = 0
          for (let i in this.subject) {
            let fenshu = code['sub_' + i]
            let credit = code['getCredit_' + i]
            if (typeof fenshu !== 'undefined') {
              // 有成绩，计算总分和总学分
              total += fenshu
              totalCredit += credit;
              // ave = Math.floor(total / count * 10) / 10 // 保留一位小数。
              // // 如果保留两位小数的话可以这样 (total / count ).toFixed(2)
            }
          }
          this.gradeList[key].totalScore = total
          this.gradeList[key].totalCredit = totalCredit;
        }

      },
      //计算排名
      getRank() {
        let _ranking = 0
        this.gradeList.sort((a, b) => {
          // 按照总分倒序
          return b.totalScore - a.totalScore
        }).forEach((item) => {
          // 设置名次
          _ranking++
          this.gradeList.find((a) => a.sno === item.sno).ranking = _ranking
        })

      },
      //获取辅导员信息,并返回专业和年级信息
      getInstructorInfo() {
        getInfo().then(res => {
          if(res.data!= null){
          this.queryParams.grade=res.guideGrade;
          let majorName = res.majorNames;
          //指导专业名称数组
          this.majorNames = res.majorNames.join(",");
          //默认第一是值，第二个是索引
          majorName.forEach((item, index) => {
            let obj = {
              id: index,
              label: item
            };
            this.majorOptions.push(obj);
            //初始化专业项
            this.queryParams.majorName = majorName[0]
            this.initMajor = majorName[0]
          });
          }
          this.getStuInfo();
        })
      },
      //获取学生信息
      getStuInfo() {
        getInfoBysno(Cookies.get("username")).then(res => {
          if (res.data != null) {
            this.stu=res.data;
            this.queryParams.grade=res.data.grade;
            this.stuClass=res.data.stuClass;
            this.majorId=res.data.majorId;
            this.$forceUpdate();
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
      // // 表单重置
      // reset() {
      //   this.form = {
      //     sno: undefined,
      //     stuName: undefined,
      //     sex: "0",
      //     certNum: undefined,
      //     collegeId: undefined,
      //     majorId: undefined,
      //     grade: undefined,
      //     stuClass: undefined,
      //     remark: undefined,
      //   };
      //   this.resetForm("form");
      // }
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
        this.queryParams.majorName = this.initMajor;
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
        // this.reset();
        // this.open = true;
        // this.title = "添加学生基础信息";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        // this.reset();
        // //重新执行专业、年级的下拉框选项
        // this.getCollege(row.collegeId, 'form')
        // const stuId = row.id || this.ids;
        // getInfo(stuId).then(response => {
        //   let data = response.data;
        //   //将年级转为int
        //   data.grade = parseInt(data.grade)
        //   this.getGrade(data.grade, 'form')
        //   this.form = data
        //   this.open = true;
        //   this.title = "修改学生基础信息";
        // });
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
        const stuIds = row.sno || this.ids;
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
        //获取学期信息
        let term = null;
        this.getDicts('valid_term').then((response) => {
          response.data.forEach((item => {
                if (this.queryParams.validTerm === item.dictValue) {
                  term = item.dictLabel
                }
              }
            )
          );
          this.$modal.confirm('确认导入"' + this.queryParams.majorName + " " + this.queryParams.startYear + "学年 " + term + '""的学生成绩吗？').then(() => {
            this.upload.title = "学生成绩信息导入";
            this.upload.open = true;

          }).catch(() => {
          })
        });

      }
      ,
      /** 下载模板操作 */
      importTemplate() {
        //先传递表头数据
        let titleNames = []
        this.header.forEach((item, index) => {
          if (index < this.header.length - 3) {
            titleNames.push(item.label)
          }

        })
        getHeader(titleNames).then(res => {
          this.download('grade/info/importTemplate', {}, `stuGrade_template_${new Date().getTime()}.xlsx`)
        })

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
      },
      // 提交上传文件
      submitFileForm() {
       //传入导入专业、学年、学期信息
        this.queryParams.titleNames=[]
        this.header.forEach((item,index)=>{
          if(index< this.header.length-3){
            this.queryParams.titleNames.push(item.label)
          }
        })
        getHeader(this.queryParams.titleNames).then(response=>{
          getParams(this.queryParams).then(res => {
            this.$refs.upload.submit();
          })
        })

      }
    }
  };
</script>
