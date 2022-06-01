<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
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
          <el-form-item label="课程名称" prop="coseName">
            <el-input
              v-model="queryParams.coseName"
              placeholder="请选择课程名称"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="开设学期" prop="validTerm">
            <el-select
              v-model="queryParams.validTerm"
              placeholder="请选择开设学期"
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
          <el-form-item label="课程性质" prop="courseType">
            <el-select
              v-model="queryParams.courseType"
              placeholder="请选择课程性质"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="dict in dict.type.course_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="开设年级" prop="grade">
            <el-select v-model="queryParams.grade" placeholder="请选择年级" clearable>
              <el-option
                v-for="dict in gradeOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="开设学年" prop="startYear">
            <el-select v-model="queryParams.startYear" placeholder="请选择学年" clearable>
              <el-option
                v-for="dict in yearsOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="课程状态"
              clearable
              style="width: 180px"
            >
              <el-option
                v-for="dict in dict.type.sys_normal_disable"
                :key="dict.value"
                :label="dict.label"
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
              v-hasPermi="['system:course:add']"
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
              v-hasPermi="['system:course:edit']"
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
              v-hasPermi="['system:course:remove']"
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
              v-hasPermi="['system:course:import']"
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
              v-hasPermi="['system:course:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="课程编号" align="center" key="coseId" prop="coseId" v-if="columns[0].visible"/>
          <el-table-column label="课程名称" align="center" key="coseName" prop="coseName" v-if="columns[1].visible"
                           :show-overflow-tooltip="true"/>
          <el-table-column label="开设年级" align="center" sortable key="grade"
                           v-if="columns[2].visible" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{scope.row.grade}}级</span>
            </template>
          </el-table-column>
          <el-table-column label="开设学年" align="center" sortable key="startYear"
                           v-if="columns[3].visible" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <span>{{scope.row.startYear}}学年</span>
            </template>
          </el-table-column>
          <el-table-column label="开设学期" align="center" key="validTerm" sortable v-if="columns[4].visible"
                           prop="validTerm" :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column label="课程学分" align="center" key="credit" prop="credit" v-if="columns[5].visible"
                           :show-overflow-tooltip="true" width="70"/>
          <el-table-column label="课程性质" align="center" key="courseType" v-if="columns[6].visible"
                           :show-overflow-tooltip="true" width="130">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.course_type" :value="scope.row.courseType"/>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" key="status" v-if="columns[7].visible">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:course:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:course:remove']"
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
            <el-form-item label="课程名称" prop="coseName">
              <el-input v-model="form.coseName" placeholder="请输入课程名称" maxlength="30"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程学分" prop="credit">
              <el-input-number v-model="form.credit" :precision="1" :step="0.5"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开设学年" prop="startYear">
              <el-select v-model="form.startYear" placeholder="请选择年级" clearable>
                <el-option
                  v-for="dict in yearsOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开设年级" prop="grade">
              <el-select v-model="form.grade" placeholder="请选择年级" clearable>
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
          <el-col :span="12">
            <el-form-item label="开设学期" prop="validTerm">
              <el-select
                v-model="form.validTerm"
                placeholder="请选择开设学期"
                clearable
                style="width: 200px"
              >
                <el-option
                  v-for="dict in dict.type.valid_term"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程性质" prop="courseType">
              <el-select
                v-model="form.courseType"
                placeholder="请选择开设学期"
                style="width: 200px"
                clearable
              >
                <el-option
                  v-for="dict in dict.type.course_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in dict.type.sys_normal_disable"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="开设专业" prop="majorIds">
              <treeselect
                :multiple="true"
                v-model="form.majorIds"
                :options="deptOptions"
                :show-count="true"
                :value-consists-of="valueConsistsOf"
                placeholder="请选择开设专业"/>
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
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <!--<div class="el-upload__tip" slot="tip">-->
          <!--<el-checkbox v-model="upload.updateSupport"/>-->
          <!--是否更新已经存在的数据-->
          <!--</div>-->
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
  import {listCourse, getCourse, addCourse, updateCourse, delCourse, changeCourseStatus} from "@/api/scholarship/course"
  import {getToken} from "@/utils/auth";
  import {treeselect} from "@/api/system/dept";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Course",
    dicts: ['sys_normal_disable', 'valid_term', 'course_type'],
    components: {Treeselect},
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
        courseList: null,
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        name: undefined,
        // majorOptions:[],
        valueConsistsOf: 'LEAF_PRIORITY',
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
        //展开的节点
        nodeKey: [],
        // 表单参数
        form: {
          validTerm: undefined,
          startYear: undefined,
          credit: 0.5,
        },
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
          // updateSupport: 0,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/system/course/importData"
        },
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: undefined,
          startYear: undefined,//默认当前年份
          validTerm: undefined,
          grade: undefined,
          status: undefined,
          collegeId: undefined,
          courseType: undefined
        },
        // 列信息
        columns: [
          {key: 0, label: `课程编号`, visible: false},
          {key: 1, label: `课程名称`, visible: true},
          {key: 2, label: `开设年级`, visible: true},
          {key: 3, label: `开设学年`, visible: true},
          {key: 4, label: `开设学期`, visible: true},
          {key: 5, label: `课程学分`, visible: true},
          {key: 6, label: `课程性质`, visible: true},
          {key: 7, label: `状态`, visible: true},
          {key: 8, label: `创建时间`, visible: true}
        ],
        // 表单校验
        rules: {
          coseName: [
            {required: true, message: "课程名称不能为空", trigger: "blur"},
            {max: 20, message: '课程名称最大长度为20', trigger: 'blur'}
          ],
          grade: [
            {required: true, message: "开设年级不能为空", trigger: ["blur", "change"]}
          ],
          startYear: [
            {required: true, message: "开设学年不能为空", trigger: "blur"}
          ],
          validTerm: [
            {required: true, message: "开设学期不能为空", trigger: ["blur", "change"]}
          ],
          majorIds: [
            {required: true, message: "开设专业不能为空", trigger: ["blur", "change"]}
          ],
          credit: [
            {required: true, message: "学分不能为空", trigger: "blur"},
            {
              validator: (rule, value, callback) => {
                if (value > 4.0) {
                  callback(new Error('学分最大为4分!!'))
                }
                else {
                  callback()
                }
              },
              trigger: ['blur', 'change']
            }
          ],
          courseType: [
            {required: true, message: "课程不能为空", trigger: ["blur", "change"]}
          ]

        }
      };
    },
    watch: {
      // 根据名称筛选部门树
      name(val) {
        this.$refs.tree.filter(val);
      }
    }
    ,
    created() {
      this.getList();
      this.getTreeselect();
    }
    ,
    methods: {
      //添加院系id的参数
      addCollegeId(params, collegeId) {
        let search = params;
        search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
        search.params['collegeId'] = collegeId;
        return search;
      }
      ,

      /**查询时-选择年份*/
      handleInput(val) {
        this.queryParams.startYear = val;
      }
      ,

      /**添加时-选择年份*/
      handleInputEdit(val) {
        this.form.startYear = val;
      }
      ,

      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listCourse(this.addCollegeId(this.queryParams, this.queryParams.collegeId)).then(response => {
            this.courseList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
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
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal.confirm('确认要"' + text + '""' + row.coseName + '"课程吗？').then(function () {
          return changeCourseStatus(row.coseId, row.status);
        }).then(() => {
          this.$modal.msgSuccess(text + "成功");
        }).catch(function () {
          row.status = row.status === "0" ? "1" : "0";
        });
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
          coseId: undefined,
          majorIds: [],
          coseName: undefined,
          grade: undefined,
          startYear: undefined,
          credit: 1.0,
          validTerm: undefined,
          status: "0",
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
        this.queryParams.collegeId = undefined
        this.handleQuery();
      }
      ,
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.coseId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.getTreeselect();
        this.open = true;
        this.title = "添加课程";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        this.getTreeselect();
        const coseId = row.coseId || this.ids;
        getCourse(coseId).then(response => {
          this.form = response.data;
          //年级转为int
          this.form.grade = parseInt(this.form.grade)
          this.form.majorIds = response.majorIds;
          this.open = true;
          this.title = "修改课程";
        });
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.coseId != undefined) {
              updateCourse(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addCourse(this.form).then(response => {
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
        const coseIds = row.coseId || this.ids;
        let params = {
          collegeId: this.queryParams.collegeId != undefined ? this.queryParams.collegeId : 0,
          coseIds: coseIds
        }
        this.$modal.confirm('是否确认删除课程编号为"' + coseIds + '"的数据项？').then(function () {
          return delCourse(params);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
      ,
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/course/export', {
          ...this.queryParams
        }, `course_${new Date().getTime()}.xlsx`)
      }
      ,
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "课程导入";
        this.upload.open = true;
      }
      ,
      /** 下载模板操作 */
      importTemplate() {
        this.download('system/course/importTemplate', {}, `course_template_${new Date().getTime()}.xlsx`)
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
  };
</script>
