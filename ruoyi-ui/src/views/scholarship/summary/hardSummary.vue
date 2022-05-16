<template>
  <div class="app-container">
    <el-row :gutter="20">
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
        <el-form-item label="学院名称" prop="collegeId">
          <el-select v-model="queryParams.collegeId" placeholder="请选择学院名称" clearable>
            <el-option
              v-for="dict in collegeOptions"
              :key="dict.collegeId"
              :label="dict.name"
              :value="dict.collegeId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
          >导出</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="loading" :data="hardSummaryList"
                :span-method="objectSpanMethod"
                border
                :summary-method="getSummaries"
                show-summary>
        <el-table-column label="学院名称" prop="collegeName" align="center" width="260"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="评定等级" prop="rankName" align="center" width="260"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="金额(元）" prop="money" align="center" width="250" sortable
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="申请人数" prop="allPeople" align="center" width="250" sortable
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column label="应发金额(元）" prop="allMoney" align="center" width="255" sortable
                         :show-overflow-tooltip="true"></el-table-column>
      </el-table>
    </el-row>
  </div>

</template>

<script>
  import {getAtLeast} from "@/api/scholarship/timeSetting"
  import {selByParentId} from "@/api/system/dept";
  import {summaryHard,getParams} from "@/api/scholarship/summary"
  import {listRank} from "@/api/scholarship/hardRank"

  export default {
    name: "hardSummary",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 表格数据
        hardSummaryList: [],
        collegeOptions: [],
        yearsOptions: [
          {id: 1, value: '2017-2018'},
          {id: 2, value: '2018-2019'},
          {id: 3, value: '2019-2020'},
          {id: 4, value: '2020-2021'},
          {id: 5, value: '2021-2022'},
          {id: 6, value: '2022-2023'},
        ],
        timeSetting: {
          setYear: undefined,
        },
        //详细奖项数量
        rankNum: undefined,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          applyYear: undefined,//申请学年
          collegeId: undefined,
        },

      }
    },
    created() {
      //获取时间参数信息
      this.getTimeSetting();
      //获取学院下拉数组
      this.getAllCollege("100");
    },
    methods: {
      //学院名称进行合并
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        if (columnIndex === 0) {
          if (rowIndex % this.rankNum === 0) {
            return {
              rowspan: this.rankNum,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
      //获取最新的时间参数信息
      getTimeSetting() {
        getAtLeast("2").then(res => {
          this.timeSetting = res.data
          this.queryParams.applyYear = this.timeSetting.setYear;
          //获取贫困等级的数量
          listRank().then(res => {
            this.rankNum = res.rows.length;
            this.getList();
          })
        })
      },
      ////获取学院下拉数组
      getAllCollege(val) {
        selByParentId(val).then(res => {
          this.collegeOptions = res.data;
        })
      },
      /** 查询用户列表 */
      getList() {
        this.loading = true;
        summaryHard(this.queryParams).then(response => {
            this.hardSummaryList = response.data;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      }
      ,
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.applyYear = this.timeSetting.setYear;
        this.handleQuery();
      },

      //表尾合计
      getSummaries(param) {
        // 定义参数接收回调数据
        const {columns, data} = param;
        const sums = [];
        columns.forEach((column, index) => {
          let values = [];
          // 使用判断遍历需要计数的列
          if (
            column.property === 'allPeople' ||
            column.property === 'allMoney'
          ) {

            values = data.map((item) => Number(item[column.property]));
          }
          if (!values.every((value) => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
          } else {
            sums[index] = '';
          }
          // 放在最后防止foreach遍历后不显示自定义名字
          if (index === 0) {
            sums[index] = '合计';
          }
        });
        return sums;
      },

      /** 导出按钮操作 */
      handleExport() {
        getParams(this.queryParams).then(res=>{
          this.download('summary/info/HardExport',{},`助学金统计数据_${new Date().getTime()}.xlsx`)
        })

      },
    }

  }
</script>

<style scoped>

</style>
