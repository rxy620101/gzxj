<template>
  <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">
    <el-tab-pane name="check" :key="'check'">
      <span slot="label"><i class="el-icon-date"></i> 待审核名单</span>
      <awardCheck :operation="activeName" v-if="check" ></awardCheck>
    </el-tab-pane>
    <el-tab-pane name="record" :key="'record'">
      <span slot="label"><i class="el-icon-date" ></i> 审核记录</span>
      <awardCheck :operation="activeName" v-if="record"></awardCheck>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import awardCheck from "@/views/scholarship/awards/awardCheck"
    export default {
        name: "checkList",
      components:{awardCheck},
      data(){
        return {
          activeName:'check',
          check:true,
          record:false,
        };
      },
      methods:{
        //标签页切换时
        handleClick(tab, event) {
          if(tab.name ==='check'){
            this.check=true;
            this.record=false;
          }
          if(tab.name ==='record'){
            this.record=true;
            this.check=false;
          }

        },

      }
    }
</script>

<style scoped>

</style>
