<template xmlns:el-col="http://www.w3.org/1999/html">
  <div class="app-container">
    <el-row :gutter="20">

      <!--学生数据-->
      <el-col :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="类型" prop="setType">
            <el-select v-model="queryParams.setType" clearable>
              <!--setType为String,则value不加：-->
              <el-option value="1" label="奖学金"></el-option>
              <el-option value="2" label="助学金"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="设置学年" prop="setYear">
            <el-select v-model="queryParams.setYear" placeholder="请选择设置学年" clearable>
              <el-option
                v-for="dict in yearsOptions"
                :key="dict.id"
                :label="dict.value"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="设置学期" prop="setTerm">
            <el-select v-model="queryParams.setTerm" placeholder="请选择设置学期" clearable
                       :disabled="queryParams.setType === '2'">
              <el-option
                v-for="dict in dict.type.valid_term"
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
              v-hasPermi="['timeSetting:info:add']"
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
              v-hasPermi="['timeSetting:info:edit']"
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
              v-hasPermi="['timeSetting:info:remove']"
            >删除
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="timeSetList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="id" prop="settingId" v-if="false" width="80"></el-table-column>
          <el-table-column label="类型" align="center" key="setType" prop="setType" v-if="columns[0].visible" width="175">
            <template slot-scope="scope">
              <span v-if="scope.row.setType == '1'">奖学金申请</span>
              <span v-if="scope.row.setType == '2'">助学金申请</span>
            </template>
          </el-table-column>
          <el-table-column label="设置学年" sortable align="center" key="setYear" v-if="columns[1].visible" width="190"
                           prop="setYear">
          </el-table-column>
          <el-table-column label="设置学期" align="center" key="setTerm" prop="setTerm"
                           v-if="columns[2].visible" :show-overflow-tooltip="true" width="180">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.valid_term" :value="scope.row.setTerm"/>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="startTime" v-if="columns[3].visible"
                           :show-overflow-tooltip="true" width="200">
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" v-if="columns[4].visible"
                           :show-overflow-tooltip="true" width="180">
          </el-table-column>
          <el-table-column width="300" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['timeSetting:info:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['timeSetting:info:remove']"
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
            <el-form-item label="设置类型" prop="setType">
              <el-radio-group v-model="form.setType" :disabled="form.settingId != undefined">
                <el-radio :label="'1'">奖学金</el-radio>
                <el-radio :label="'2'">助学金</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设置学年" prop="setYear">
              <el-select v-model="form.setYear" placeholder="请选择设置学年" :disabled="form.settingId != undefined"
                         clearable>
                <el-option
                  v-for="dict in yearsOptions"
                  :key="dict.id"
                  :label="dict.value"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker clearable
                              v-model="form.startTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择开始时间"
                              style="width: 200px"
                              :picker-options="startTimeOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker clearable
                              v-model="form.endTime"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择结束时间"
                              :picker-options="endTimeOptions"
                              style="width: 200px">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设置学期" prop="setTerm" v-if="form.setType == '1'">
              <el-select v-model="form.setTerm" placeholder="请选择学期" :disabled="form.settingId != undefined"
                         clearable>
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
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"
              ></el-input>
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
  import {listSetting, getSetting, delSetting, addSetting, updateSetting} from "@/api/scholarship/timeSetting";

  export default {
    name: "timeSetting",
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
        timeSetList: null,
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

        // 表单参数
        form: {},
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          setType: undefined,
          setYear: undefined,//申请学年
          setTerm: undefined,//申请学期
        },
        // 列信息
        columns: [
          {key: 0, label: `设置类型`, visible: true},
          {key: 1, label: `设置学年`, visible: true},
          {key: 2, label: `设置学期`, visible: true},
          {key: 3, label: `开始时间`, visible: true},
          {key: 4, label: `结束时间`, visible: true},
        ],
        //开始时间小于结束时间，结束时间大于开始时间
        startTimeOptions: {
          disabledDate: time => {
            const endDateVal = new Date(this.form.endTime).getTime()
            if (endDateVal) {
              return time.getTime() > endDateVal - 0
            }
          },
        },
        endTimeOptions: {
          disabledDate: time => {
            const beginDateVal = new Date(this.form.startTime).getTime()
            if (beginDateVal) {
              return time.getTime() < beginDateVal - 0
            }
          }
        },
        // 表单校验
        rules: {
          setYear: [{required: true, message: "学年不能为空!", trigger: ["blur", "change"]},],
          endTime: [
            {required: true, message: "开始时间不能为空!", trigger: ["blur", "change"]},
          ],
          startTime: [
            {required: true, message: "结束时间不能为空!", trigger: ["blur", "change"]},
          ],
          setTerm: [{
            validator: (rule, value, callback) => {
              //助学金
              if (this.form.setType == '2') {
                callback()
              }
              //奖学金
              else {
                if (value == undefined) {
                  callback("学期不能为空!")
                }
                else {
                  callback()
                }
              }
            },
            trigger: ["blur", "change"]
          }],
        }
      };
    },
    watch: {}
    ,
    created() {
      this.getList()
    }
    ,
    methods: {
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listSetting(this.queryParams).then(response => {
            this.timeSetList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },

      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          settingId: undefined,
          setType: '1',
          setYear: undefined,
          setTerm: undefined,
          startTime: undefined,
          endTime: undefined,
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
        this.ids = selection.map(item => item.settingId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      }
      ,

      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "新增时间参数设置";
      }
      ,
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        //重新执行专业、年级的下拉框选项
        // this.getCollege(row.collegeId, 'form')
        const stuId = row.settingId || this.ids;
        getSetting(stuId).then(response => {
          let data = response.data;
          this.form = data;
          this.open = true;
          this.title = "修改时间参数设置"
        })
      }
      ,
      /** 提交按钮 */
      submitForm: function () {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.settingId != undefined) {
              updateSetting(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addSetting(this.form).then(response => {
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
        const stuIds = row.settingId || this.ids;
        this.$modal.confirm('是否确认删除这些数据项？').then(function () {
          return delSetting(stuIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },
    }
  };
</script>
