<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="学生姓名" prop="stuName">
            <el-input
              v-model="queryParams.stuName"
              placeholder="请输入学生姓名"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable>
              <el-option
                v-for="dict in dict.type.sys_user_sex"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="学院名称" prop="collegeId">
            <el-select v-model="queryParams.collegeId" placeholder="请选择学院名称" clearable @change="getCollege($event,'queryParams')">
              <el-option
                v-for="dict in collegeOptions"
                :key="dict.collegeId"
                :label="dict.name"
                :value="dict.collegeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="专业名称" prop="majorId">
            <el-select v-model="queryParams.majorId" placeholder="请选择专业名称" clearable @change="getMajor($event,'queryParams')">
              <el-option
                v-for="dict in majorOptions"
                :key="dict.collegeId"
                :label="dict.name"
                :value="dict.collegeId"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="年级" prop="grade">
            <el-select v-model="queryParams.grade" placeholder="请选择年级" clearable @change="getGrade($event,'queryParams')">
              <el-option
                v-for="dict in gradeOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="班级" prop="stuClass">
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
              v-hasPermi="['stu:info:add']"
            >新增
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
              v-hasPermi="['stu:info:edit']"
            >修改
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
              v-hasPermi="['stu:info:remove']"
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
              v-hasPermi="['stu:info:import']"
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
              v-hasPermi="['stu:info:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="stuInfoList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="id" v-if="false" width="20"></el-table-column>
          <el-table-column label="学号" align="center" key="sno" prop="sno" v-if="columns[0].visible" width="100"  />
          <el-table-column label="姓名" align="center" key="stuName" prop="stuName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="80"/>
          <el-table-column label="性别" align="center" key="sex" v-if="columns[2].visible" width="60">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
            </template>
          </el-table-column>
          <el-table-column label="学院" align="center" sortable key="collegeName" prop="collegeName"
                           v-if="columns[3].visible" :show-overflow-tooltip="true" width="175">
          </el-table-column>
          <el-table-column label="专业" align="center" sortable key="majorName" prop="majorName"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="175">
          </el-table-column>
          <el-table-column label="年级" align="center" sortable key="grade"
                           v-if="columns[5].visible" :show-overflow-tooltip="true" width="70">
            <template slot-scope="scope">
              <span>{{scope.row.grade}}级</span>
            </template>
          </el-table-column>
          <el-table-column label="班级" align="center" sortable key="stuClass" prop="stuClass"
                           v-if="columns[6].visible" :show-overflow-tooltip="true" >
          </el-table-column>
          <el-table-column label="入学时间" align="center" prop="registerTime" v-if="columns[7].visible"
                           :show-overflow-tooltip="true">
            <!--<template slot-scope="scope">-->
              <!--<span>{{ parseTime(scope.row.registerTime) }}</span>-->
            <!--</template>-->
          </el-table-column>
          <el-table-column>
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['stu:list:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['stu:list:remove']"
              >删除
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
            <el-form-item label="学号" prop="sno">
              <el-input v-model="form.sno" placeholder="请输入学号" :disabled="form.id != undefined"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学生姓名" prop="stuName">
              <el-input v-model="form.stuName" placeholder="请输入学生姓名" :disabled="this.form.id != undefined"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择性别" :disabled="this.form.id != undefined">
                <el-option
                  v-for="dict in dict.type.sys_user_sex"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="certNum">
              <el-input v-model="form.certNum" placeholder="请输入身份证号" :disabled="this.form.id != undefined"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学院名称" prop="collegeId">
              <el-select v-model="form.collegeId" placeholder="请选择学院名称" clearable @change="getCollege($event,'form')">
                <el-option
                  v-for="dict in collegeOptions"
                  :key="dict.collegeId"
                  :label="dict.name"
                  :value="dict.collegeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="专业名称" prop="majorId">
              <el-select v-model="form.majorId" placeholder="请选择专业名称" clearable>
                <el-option
                  v-for="dict in majorOptions"
                  :key="dict.collegeId"
                  :label="dict.name"
                  :value="dict.collegeId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="年级" prop="grade">
              <el-select v-model="form.grade" placeholder="请选择年级" clearable @change="getGrade($event,'form')">
                <el-option
                  v-for="dict in gradeOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="班级" prop="stuClass">
              <el-select v-model="form.stuClass" placeholder="请选择班级" clearable>
                <el-option
                  v-for="dict in classOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="入学时间" prop="registerTime">
              <el-date-picker clearable
                              v-model="form.registerTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择入学时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
  import {listInfo, getInfo, delInfo, addInfo, updateInfo, selectGrade, selectClass,checkSno} from "@/api/scholarship/stuInfo";
  import {getToken} from "@/utils/auth";
  import {treeselect, selByParentId} from "@/api/system/dept";

  export default {
    name: "StuBaseInfo",
    dicts: ['sys_user_sex'],
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
        stuInfoList: null,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        collegeOptions: [],
        majorOptions: [],
        gradeOptions: [
          {id:1 ,value: 2017},
          {id:2 ,value: 2018},
          {id:3 ,value: 2019},
          {id:4 ,value: 2020},
          {id:5 ,value: 2021},
          {id:6 ,value: 2022},
        ],
        classOptions: [],

        // 表单参数
        form: {
          id:undefined,
          sno:undefined,
          stuName:undefined,
          sex:"0",
          collegeId:undefined,
          majorId:undefined,
          grade:undefined,
          stuClass:undefined,
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
          sex: undefined,
          certNum: undefined,
          collegeId: undefined,
          majorId: undefined,
          grade: undefined,
          stuClass: undefined,
        },
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
        rules: {
          sno: [
            {required: true, message: "学号不能为空", trigger: "blur"},
            {
              validator: (rule, value, callback) => {
                if(this.form.id == undefined){
                  if (value.length !=10 ) {
                    callback(new Error('学号的长度为10位!!'))
                  }
                  else {
                    let message=undefined;
                    checkSno(value).then(res=>{
                      if(res.data != undefined){
                        message='该学号已存在!!'
                        callback(new Error(message))
                      }
                      else{
                        callback()
                      }
                    })
                  }
                }
                else{
                  callback()
                }

              },
              trigger: "blur"
            }
          ],
          stuName: [
            {required: true, message: "姓名不能为空", trigger: "blur"}
          ],
          certNum: [
            {required: true, message: "身份证不能为空", trigger: "blur"},
            //min max 限制程度
            {
              validator: (rule, value, callback) => {
                if (value.length !=18 ) {
                  callback(new Error('身份证号为18位!!'))
                }
                else {
                  callback()
                }
              },
              trigger: "blur"
            },
          ],
          collegeId: [{required: true, message: "学院名称不能为空", trigger: ["blur", "change"]}],
          majorId: [{required: true, message: "专业名称不能为空", trigger: ["blur", "change"]}],
          grade: [
            {required: true, message: "年级不能为空", trigger: ["blur", "change"]}
          ],
          stuClass: [{required: true, message: "班级不能为空", trigger: ["blur", "change"]}],
          registerTime: [{required: true, message: "入学时间不能为空", trigger: "blur"}],
        }
      };
    },
    watch: {
    }
    ,
    created() {
      this.getList();
      //获取学院 100是顶级
      this.getByParentId(100).then(res => {
        this.collegeOptions = res;
      });
    }
    ,
    methods: {

      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listInfo(this.queryParams).then(response => {
            this.stuInfoList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },

      //查询学院或专业信息
      async getByParentId(parentId) {
        //100 是顶级 用回调函数接收 async 声明为异步方法，await等待异步执行完毕
        let data = [];
        await selByParentId(parentId).then(res => {
          data = res.data;
        });
        return data;
      },

      //学院下拉框
      getCollege(val,reset) {
       if(reset=='form'){
         this.form.majorId=undefined;
       }
       else{
         this.queryParams.majorId=undefined;
       }
        if(val!= null && val != ""){
          this.getByParentId(val).then(res => {
            this.majorOptions = res;
          });
        }
      },
      //数组处理
      getArrayData(array) {
        let newArray = [];
        array.forEach((item, index) => {
          var obj = {};
          obj.id = index + 1;
          obj.value = item;
          newArray.push(obj);
        });
        return newArray
      },

      getGrade(val,reset) {
        if(reset=='form'){
          this.form.stuClass=undefined;
        }
        else{
          this.queryParams.stuClass=undefined;
        }
        if(val!=null && val !=""){
          selectClass(val).then(res => {
            let classData=res.data;
            this.classOptions = this.getArrayData(classData);
          });
        }
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
        this.ids = selection.map(item => item.id);
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
        this.getCollege(row.collegeId,'form')
        const stuId = row.id || this.ids;
        getInfo(stuId).then(response => {
          let data = response.data;
          //将年级转为int
          data.grade=parseInt(data.grade)
          this.getGrade(data.grade,'form')
          this.form=data
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
