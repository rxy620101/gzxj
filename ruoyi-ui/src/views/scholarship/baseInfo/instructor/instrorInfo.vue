<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="25">
        <div class="head-container">
          <el-input
            v-model="name"
            placeholder="请输入院系/专业名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            node-key="id"
            :default-expanded-keys="nodeKey"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--课程数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="姓名" prop="instructorName">
            <el-input
              v-model="queryParams.instructorName"
              placeholder="辅导员姓名"
              clearable
              style="width: 240px"
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
          <el-form-item label="指导年级" prop="guideGrade">
            <el-select
              v-model="queryParams.guideGrade"
              placeholder="请选择指导年级"
              clearable
              style="width: 180px"
            >
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
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['instructor:info:add']"
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
              v-hasPermi="['instructor:course:edit']"
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
              v-hasPermi="['instructor:info:remove']"
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
              v-hasPermi="['instructor:info:import']"
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
              v-hasPermi="['instructor:info:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="instructorList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="id" align="center" v-if="false"/>
          <el-table-column label="职工号" align="center" key="instructorId" prop="instructorId" v-if="columns[6].visible"
                           width="110"/>
          <el-table-column label="姓名" align="center" key="instructorName" prop="instructorName"
                           v-if="columns[0].visible" :show-overflow-tooltip="true" width="80"/>
          <el-table-column label="性别" align="center" key="sex" v-if="columns[1].visible"
                           :show-overflow-tooltip="true" width="50">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.sex"/>
            </template>
          </el-table-column>
          <el-table-column label="联系方式" align="center" key="tel" prop="tel" v-if="columns[2].visible"
                           :show-overflow-tooltip="true" width="110"/>
          <el-table-column label="所属学院" align="center" sortable key="collegeName" prop="collegeName"
                           v-if="columns[3].visible" :show-overflow-tooltip="true" width="110">
          </el-table-column>
          <el-table-column label="指导年级" align="center" sortable key="guideGrade" prop="guideGrade"
                           v-if="columns[4].visible" :show-overflow-tooltip="true" width="100">
          </el-table-column>
          <el-table-column label="指导专业" align="center" sortable key="guideMajorIds"
                           v-if="columns[5].visible" :show-overflow-tooltip="true" prop="guideMajorIds"
          >
          </el-table-column>
          <el-table-column>
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['instructor:info:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['instructor:info:remove']"
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
            <el-form-item label="职工号" prop="instructorId">
              <el-input v-model="form.instructorId" placeholder="请输入职工号" maxlength="10"
                        :disabled="this.form.id != undefined"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="instructorName">
              <el-input v-model="form.instructorName" placeholder="请输入辅导员姓名" maxlength="20"
                        :disabled="this.form.id != undefined"/>
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
            <el-form-item label="联系电话" prop="tel">
              <el-input v-model="form.tel" placeholder="请输入联系电话"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="学院名称" prop="collegeId">
              <el-select v-model="form.collegeId" placeholder="请选择学院名称" clearable @change="getCollege">
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
            <el-form-item label="指导年级" prop="guideGrade">
              <el-select v-model="form.guideGrade" placeholder="请选择指导年级" clearable>
                <el-option
                  v-for="dict in gradeOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="指导专业" prop="majorIds">
              <el-select v-model="form.majorIds" placeholder="请选择指导专业" clearable
                         style="width: 100%" multiple @change="getMajors">
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
  import {listInfo, getInfo, addInfo, updateInfo, delInfo, checkUnique} from "@/api/scholarship/instrorInfo"
  import {getToken} from "@/utils/auth";
  import {treeselect, selByParentId, selMajorNames} from "@/api/system/dept";

  export default {
    name: "instrorInfo",
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
        instructorList: [],
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        name: undefined,
        //展开的节点
        nodeKey: [],
        collegeOptions: [],
        gradeOptions: [
          {id: 1, value: 2017},
          {id: 2, value: 2018},
          {id: 3, value: 2019},
          {id: 4, value: 2020},
          {id: 5, value: 2021},
          {id: 6, value: 2022},
        ],
        majorOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
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
          url: process.env.VUE_APP_BASE_API + "/instructor/info/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          instructorName: undefined,
          sex: undefined,
          guideGrade: undefined,
          collegeId: undefined,
        },
        // 列信息
        columns: [
          {key: 0, label: `姓名`, visible: true},
          {key: 1, label: `性别`, visible: true},
          {key: 2, label: `联系电话`, visible: true},
          {key: 3, label: `所属学院`, visible: true},
          {key: 4, label: `指导年级`, visible: true},
          {key: 5, label: `指导专业`, visible: true},
          {key: 6, label: `职工号`, visible: true},
        ],
        // 表单校验
        rules: {
          instructorId: [
            {required: true, message: "职工号不能为空", trigger: "blur"},
            {
              validator: (rule, value, callback) => {
                if (this.form.id == undefined) {
                  if (value.length != 10) {
                    callback(new Error('职工号的长度为10位!!'))
                  }
                  else {
                    let message = undefined;
                    checkUnique(value).then(res => {
                      if (res.data != undefined) {
                        message = '该职工号已存在!!'
                        callback(new Error(message))
                      }
                      else {
                        callback()
                      }
                    })
                  }
                }
                else {
                  callback()
                }

              },
              trigger: "blur",
            }
          ],
          instructorName: [
            {required: true, message: "辅导员姓名不能为空！", trigger: "blur"},
            {max: 20, message: '姓名最大长度为20', trigger: 'blur'}
          ],
          tel:
            [
              {
                pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
                message: "手机号码格式不正确！！",
                trigger: "blur",
              }
            ],

          collegeId:
            [
              {required: true, message: "所属学院不能为空", trigger: ["blur", "change"]}
            ],
          guideGrade:
            [
              {required: true, message: "指导年级不能为空", trigger: ["blur", "change"]}
            ],
          majorIds:
            [
              {required: true, message: "指导专业不能为空", trigger: ["blur", "change"]}
            ]

        }
      };
    },
    watch: {
      // 根据名称筛选部门树
      name(val) {
        this.$refs.tree.filter(val);
      }
      ,
    }
    ,
    created() {
      this.getList();
      this.getTreeselect();
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
            //this.instructorList = response.rows;
            let dataList = response.rows;
            this.total = response.total;
            this.loading = false;
            //处理专业名称
            dataList.forEach(item => {
              let majorIds = item.guideMajorIds.split(",")
              selMajorNames(majorIds).then(res => {
                let majorNames = res.data.join(",")
                item.guideMajorIds = majorNames;
              });
            })
            this.instructorList = dataList;

          }
        );
      }
      ,
      //查询学院或专业信息
      async getByParentId(parentId) {
        //100 是顶级 用回调函数接收 async 声明为异步方法，await等待异步执行完毕
        let data = [];
        await
          selByParentId(parentId).then(res => {
            data = res.data;
          });
        return data;
      }
      ,
      getCollege(val) {
        this.form.collegeId = val;
        this.form.majorIds = [];
        if (val != null && val != "") {
          this.getByParentId(val).then(res => {
            this.majorOptions = res;
          });
        }
      }
      ,
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.deptOptions = response.data;
          this.nodeKey.push(this.deptOptions[0].id)
        });
      }
      ,
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      }
      ,
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.collegeId = data.id;
        this.handleQuery();
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
          id: undefined,
          instructorId: undefined,
          instructorName: undefined,
          sex: "0",
          tel: undefined,
          guideGrade: undefined,
          guideMajorIds: [],
          majorIds: [],
          collegeId: undefined,
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
        //只重置form里的
        this.resetForm("queryForm");
        this.queryParams.collegeId=undefined
        this.handleQuery();
      }
      ,
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.instructorId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加辅导员信息";
      }
      ,
      getMajors(val) {
        //强制更新
        this.$forceUpdate()
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.instructorId || this.ids;
        getInfo(id).then(response => {
          this.form= response.data;
          this.form.guideGrade=parseInt(this.form.guideGrade)
          //专业id转int数组
          this.getCollege(this.form.collegeId)
          let data = this.form.guideMajorIds.split(",");
         // let listData = []
          data.forEach(item => {
            this.form.majorIds.push(+item)
          })
          this.open = true;
          this.title = "修改辅导员信息";
        });
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            //数组转字符串用join 字符串转数组用split
            this.form.guideMajorIds = this.form.majorIds.join(",");
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
        const instructorId = row.instructorId || this.ids;
        this.$modal.confirm('是否确认删除辅导员"' + row.instructorName + '"的数据项？').then(function () {
          return delInfo(instructorId);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
      ,
      /** 导出按钮操作 */
      handleExport() {
        this.download('instructor/info/export', {
          ...this.queryParams
        }, `instructorInfo_${new Date().getTime()}.xlsx`)
      }
      ,
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "辅导员信息导入";
        this.upload.open = true;
      }
      ,
      /** 下载模板操作 */
      importTemplate() {
        this.download('instructor/info/importTemplate', {}, `辅导员基础信息_${new Date().getTime()}.xlsx`)
      }
      ,
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
  }
  ;
</script>
