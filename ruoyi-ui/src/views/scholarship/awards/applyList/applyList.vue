<template>
  <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">
    <el-tab-pane name="apply" :key="'apply'">
      <span slot="label"><i class="el-icon-date"></i> 奖学金申请</span>
      <awardApply :operation="activeName" v-if="apply" ></awardApply>
    </el-tab-pane>
    <el-tab-pane name="record" :key="'record'">
      <span slot="label"><i class="el-icon-date" ></i> 学生申请记录</span>
      <awardApply :operation="activeName" v-if="record"></awardApply>
    </el-tab-pane>
  </el-tabs>

</template>

<script>
  import awardApply from "@/views/scholarship/awards/awardApply"

  export default {
    name: "applyList",
    components:{awardApply},
    data(){
      return {
        activeName:'apply',
        apply:true,
        record:false,
      };
    },
    methods:{
      //标签页切换时
      handleClick(tab, event) {
       if(tab.name ==='apply'){
         this.apply=true;
         this.record=false;
       }
       if(tab.name ==='record'){
         this.record=true;
         this.apply=false;
       }

      },


    }

  }
</script>

<style scoped>

</style>
