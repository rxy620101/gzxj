<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="奖项类别" prop="parentId">
        <el-select v-model="queryParams.parentId" placeholder="请输入奖项类别" clearable @change="getType">
          <el-option
            v-for="dict in awardTypeOptions"
            :key="dict.settingId"
            :label="dict.awardNames"
            :value="dict.settingId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="具体奖项" prop="settingId">
        <el-select v-model="queryParams.settingId" placeholder="请输入对应具体奖项" clearable>
          <el-option
            v-for="dict in awardNamesOpitons"
            :key="dict.settingId"
            :label="dict.awardNames"
            :value="dict.settingId"
          />
        </el-select>
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
          v-hasPermi="['award:info:add']"
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
      :data="awardList"
      row-key="settingId"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="awardNames" label="奖项名称" width="200"></el-table-column>
      <el-table-column prop="setYear" label="设置学年" align="center" width="230"></el-table-column>
      <!--<el-table-column prop="orderNum" label="排序" width="200"></el-table-column>-->
      <el-table-column prop="setTerm" label="设置学期" align="center"></el-table-column>
      <el-table-column prop="num" label="名额" align="center" width="120">
        <template slot-scope="scope">
         <span v-if="scope.row.num != null">{{scope.row.num}}个</span>
        </template>
      </el-table-column>
      <el-table-column prop="ratio" label="占比" align="center" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.ratio != null">{{scope.row.ratio}}%</span>
        </template>
      </el-table-column>
      <el-table-column prop="money" label="奖项金额" align="center" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.money != 0">{{scope.row.money}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" align="center" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="220">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['award:info:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
            v-hasPermi="['award:info:add']"
            v-if="scope.row.setYear== null"
          >新增
          </el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['award:info:remove']"
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
              <treeselect v-model="form.parentId" :options="awardTyepeOptions" :normalizer="normalizer"
                          placeholder="选择奖项类别"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="新增类型" >
              <el-radio-group v-model="form.addType" @change="$forceUpdate()">
                <el-radio :label="0">类别</el-radio>
                <el-radio :label="1">奖项</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="名称" prop="awardNames">
              <el-input v-model="form.awardNames" placeholder="名称"/>
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
            <el-form-item label="设置学年" prop="setYear" v-if="form.addType == 1">
              <el-select v-model="form.setYear" placeholder="请选择设置学年">
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
            <el-form-item label="设置学期" prop="setTerm" v-if="form.addType == 1">
              <el-select v-model="form.setTerm" placeholder="请选择设置学期" clearable>
                <el-option
                  v-for="dict in dict.type.valid_term"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
          <el-row v-if="form.addType == 1">
            <el-col :span="12">
              <el-form-item label="分配方式">
                <el-radio-group v-model="form.assignWay" @change="$forceUpdate()">
                  <el-radio :label="0">名额</el-radio>
                  <el-radio :label="1">占比</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="占比(%)" prop="ratio" v-if="form.assignWay == 1">
                <el-input-number v-model="form.ratio" controls-position="right" :min="0" :step="0.1" :max="100"/>
              </el-form-item>
              <el-form-item label="名额" prop="num" v-if="form.assignWay == 0">
                <el-input-number v-model="form.num" controls-position="right" :min="1" :step="1"/>
              </el-form-item>
            </el-col>
          </el-row>
        <el-row>
          <el-col :span="12" v-if="form.addType == 1">
            <el-form-item label="奖项金额" prop="money">
              <el-input-number v-model="form.money" controls-position="right" :step="1"></el-input-number>
            </el-form-item>

          </el-col>
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

  import {
    listAward,
    getAward,
    delAward,
    addAward,
    updateAward,
    checkByName,
    selByParentId,
    listExcludeChild
  } from "@/api/scholarship/awardSetting";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import Cookies from "js-cookie";

  export default {
    name: "awardSetting",
    dicts: ['sys_normal_disable', 'valid_term'],
    components: {Treeselect},
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 表格树数据
        awardList: [],
        // 部门树选项
        awardTyepeOptions: [],
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
          parentId: undefined,
          settingId: undefined,
          status: undefined
        },
        // 表单参数
        form: {},
        awardTypeOptions: [],
        awardNamesOpitons: [],
        yearsOptions: [
          {id: 1, value: '2017-2018'},
          {id: 2, value: '2018-2019'},
          {id: 3, value: '2019-2020'},
          {id: 4, value: '2020-2021'},
          {id: 5, value: '2021-2022'},
          {id: 6, value: '2022-2023'},
        ],

        // 表单校验
        rules: {
          parentId: [
            {required: true, message: "奖项类别不能为空", trigger: "blur"}
          ],

          awardNames: [
            {required: true, message: "名称不能为空", trigger: "blur"},
            {
              validator: (rule, value, callback) => {
                if (this.form.settingId == undefined) {
                  checkByName(value).then(res => {
                    if (res.rows == 1) {
                      callback(new Error("该名称已存在!"))
                    }
                    else {
                      callback()
                    }
                  })
                }
                else {
                  callback()
                }
              },
              trigger: "blur"
            }
          ],
          setYear: [{
            validator: (rule, value, callback) => {
              if (this.form.addType == 0) {
                callback()
              }
              else {
                if (value == undefined) {
                  callback("设置学年不能为空")
                }
                else {
                  callback()
                }
              }
            },
            trigger: ['blur','change']
          }
          ],
          setTerm: [{
            validator: (rule, value, callback) => {
              if (this.form.addType == 0) {
                callback()
              }
              else {
                if (value == undefined) {
                  callback("设置学期不能为空")
                }
                else {
                  callback()
                }
              }
            },
            trigger: ['blur','change']
          }
          ],
          orderNum: [
            {required: true, message: "显示排序不能为空", trigger: "blur"}
          ],
          ratio: [
            {
              validator: (rule, value, callback) => {
                if (this.form.addType == 0) {
                  callback()
                }
                else {
                  if (value == 0 && this.form.assignWay == 1) {
                    callback("对应分数不能为0!")
                  }
                  else {
                    callback();
                  }
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
      this.getType("1");
    }
    ,
    methods: {

      //奖项类别初始化
      getType(val) {
        this.queryParams.settingId = undefined;
        if (val != '') {
          selByParentId(val).then(res => {
            let dataList = res.data;
            let dataArray = []
            dataList.forEach(item => {
              dataArray.push({
                settingId: item.settingId,
                awardNames: item.awardNames
              })
            })
            if (val == "1") {
              this.awardTypeOptions = dataArray;
            }
            else {
              this.awardNamesOpitons = dataArray;
            }
          })
        }
      },

      /** 查询部门列表 */
      getList() {
        this.loading = true;
        listAward(this.queryParams).then(response => {
          this.awardList = this.handleTree(response.rows, "settingId");
          this.loading = false;
        });
      }
      ,

      /** 转换部门数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.settingId,
          label: node.awardNames,
          children: node.children
        };
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
          settingId: undefined,
          parentId: undefined,
          awardNames: undefined,
          setYear: undefined,
          setTerm: undefined,
          orderNum: undefined,
          addType: 0,
          ratio: 0,
          status: "0",
          assignWay:0,
          num:1,
          money:100,
        };
        this.resetForm("form");
      }
      ,
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList();
      }
      ,

      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryParams");
        this.handleQuery();
      }
      ,

      /** 新增按钮操作 */
      handleAdd(row) {
        this.reset();
        if (row != undefined) {
          this.form.parentId = row.settingId;
        }
        this.open = true;
        this.title = "添加奖项/类别";
        listAward().then(response => {
          this.awardTyepeOptions = this.handleTree(response.rows, "settingId");
        });
      }
      ,
      /** 展开/折叠操作 */
      toggleExpandAll() {
        this.refreshTable = false;
        this.isExpandAll = !this.isExpandAll;
        this.$nextTick(() => {
          this.refreshTable = true;
        });
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        getAward(row.settingId).then(response => {
          this.form = response.data;
          if (response.data.ratio > 0 || response.data.num >0) {
            //是奖项
            this.form.addType = 1
          }
          else {
            this.form.addType = 0
          }
          if(response.data.ratio == null){
            //名额方式
            this.form.assignWay=0
          }
          else{
            this.form.assignWay=1
          }
          this.open = true;
          this.title = "修改奖项/类别";
        });
        listExcludeChild(row.settingId).then(response => {
          this.awardTyepeOptions = this.handleTree(response.data, "settingId");
        });
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.settingId != undefined) {
              updateAward(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              //名额方式
              if(this.form.addType==0 || this.form.assignWay==0){
                this.form.ratio=undefined;
              }
              if(this.form.addType==0 || this.form.assignWay==1){
                this.form.num=undefined;
              }
              addAward(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除名称为"' + row.awardNames + '"的数据项？').then(function () {
          return delAward(row.settingId);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      }
    }
  };
</script>
