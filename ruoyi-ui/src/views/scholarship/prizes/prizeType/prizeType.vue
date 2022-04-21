<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="奖项/类别名称" prop="prizeName">
        <el-input
          v-model="queryParams.typeName"
          placeholder="请输入奖项/类别名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="奖项状态" clearable>
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
          v-hasPermi="['prizeType:info:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="prizeTypeList"
      row-key="typeId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="typeName" label="奖项名称" width="300" ></el-table-column>
      <el-table-column prop="extraPoint" label="对应分数" align="center"></el-table-column>
      <!--<el-table-column prop="orderNum" label="排序" width="200"></el-table-column>-->
      <el-table-column prop="status" label="状态" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="250">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="400" v-if="userName == 'admin'">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['prizeType:info:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['prizeType:info:add']"
            v-if="scope.row.extraPoint == 0"
          >新增
          </el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['prizeType:info:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="奖项类别" prop="parentId">
              <treeselect v-model="form.parentId" :options="prizeTypeOptions" :normalizer="normalizer" placeholder="选择奖项类别"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="新增类型" prop="addType">
              <el-radio-group v-model="form.addType" @change="$forceUpdate()">
                <el-radio :label="0">类别</el-radio>
                <el-radio :label="1">奖项</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="typeName">
              <el-input v-model="form.typeName" placeholder="名称"/>
            </el-form-item>
            <el-form-item label="对应分数" prop="extraPoint" v-if="form.addType == 1">
              <el-input-number v-model="form.extraPoint" controls-position="right" :min="0" :step="0.1"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0"/>
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
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import {listPrizetype, getPrizetype, delPrizetype, addPrizetype, updatePrizetype,selByParentId,checkByName,updateStatus,listExcludeChild} from "@/api/scholarship/prizeType";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import Cookies from "js-cookie";

  export default {
    name: "prizeType",
    dicts: ['sys_normal_disable'],
    components: {Treeselect},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 表格树数据
        prizeTypeList: [],
        // 部门树选项
        prizeTypeOptions: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 是否展开，默认全部展开
        isExpandAll: false,
        // 重新渲染表格状态
        refreshTable: true,
        // 查询参数
        queryParams: {
          typeName: undefined,
          status: undefined
        },
        // 表单参数
        form: {
          typeName:undefined,
        },
        userName:Cookies.get("username"),
        // 表单校验
        rules: {
          parentId: [
            {required: true, message: "奖项类别不能为空", trigger: "blur"}
          ],
          typeName: [
            {required: true, message: "名称不能为空", trigger: "blur"},
            {
              validator: (rule, value, callback) => {
                if(this.form.typeId == undefined){
                    checkByName(value).then(res=>{
                      if(res.rows == 1){
                        callback(new Error("该名称已存在!"))
                      }
                      else{
                        console.log(111)
                        callback()
                      }
                    })
                }
                else{
                  callback()
                }
              },
              trigger: "blur"
            }
          ],
          orderNum: [
            {required: true, message: "显示排序不能为空", trigger: "blur"}
          ],
          extraPoint: [
            {
              validator: (rule, value, callback) => {
                if(this.form.addType == 1){
                  callback()
                }
               else{
                  callback("对应分数不能为0!")
                }

              },
              trigger: "blur"
            }
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {

      /** 查询部门列表 */
      getList() {
        this.loading = true;
        listPrizetype(this.queryParams).then(response => {
          this.prizeTypeList = this.handleTree(response.rows, "typeId");
          this.loading = false;
        });
      },

      /** 转换部门数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.typeId,
          label: node.typeName,
          children: node.children
        };
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          typeId: undefined,
          parentId: undefined,
          typeName: undefined,
          orderNum: undefined,
          addType:0,
          extraPoint:0,
          status: "0"
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      },

      /** 重置按钮操作 */
      resetQuery() {
        this.queryParams = {
          typeName: undefined,
          status: undefined
        }
        this.resetForm("queryParams");
        this.handleQuery();
      },

      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        if (row != undefined) {
          this.form.parentId = row.typeId;
        }
        this.open = true;
        this.title = "添加奖项/类别";
        listPrizetype().then(response => {
          this.prizeTypeOptions = this.handleTree(response.rows, "typeId");
        });
      },
      /** 展开/折叠操作 */
      toggleExpandAll() {
        this.refreshTable = false;
        this.isExpandAll = !this.isExpandAll;
        this.$nextTick(() => {
          this.refreshTable = true;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        getPrizetype(row.typeId).then(response => {
          this.form = response.data;
          if(response.data.extraPoint>0){
            //是奖项
            this.form.addType=1
          }
          else{
            this.form.addType=0
          }
          this.open = true;
          this.title = "修改奖项/类别";
        });
        listExcludeChild(row.typeId).then(response => {
          this.prizeTypeOptions = this.handleTree(response.data, "typeId");
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.typeId != undefined) {
              updatePrizetype(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addPrizetype(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$modal.confirm('是否确认删除名称为"' + row.typeName + '"的数据项？').then(function () {
          return delPrizetype(row.typeId);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
    }
  };
</script>
