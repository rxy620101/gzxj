<template>
  <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">
    <el-tab-pane name="gradeImport" :key="'gradeImport'">
      <span slot="label"><i class="el-icon-date"></i> 成绩导入</span>
      <GradeImport  v-if="gradeImport" ></GradeImport>
    </el-tab-pane>
    <el-tab-pane name="gradeCheck" :key="'gradeCheck'">
      <span slot="label"><i class="el-icon-date" ></i> 申诉记录</span>
      <AppealInfo  v-if="gradeCheck"></AppealInfo>
    </el-tab-pane>
  </el-tabs>

</template>

<script>
  import GradeImport from "@/views/scholarship/grade/gradeImport"
  import AppealInfo from "@/views/scholarship/grade/appealInfo"

  export default {
    name: "index",
    components:{GradeImport,AppealInfo},
    data(){
      return {
        activeName:'gradeImport',
        gradeImport:true,
        gradeCheck:false,
      };
    },
    methods:{
      //标签页切换时
      handleClick(tab, event) {
        if(tab.name ==='gradeImport'){
          this.gradeImport=true;
          this.gradeCheck=false;
        }
        if(tab.name ==='gradeCheck'){
          this.gradeImport=false;
          this.gradeCheck=true;
        }

      },


    }

  }
</script>

<style scoped>

</style>
