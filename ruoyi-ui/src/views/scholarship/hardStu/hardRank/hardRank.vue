<template>
  <el-form :model="form" label-width="80px">
    <el-row>
      <el-col :span="12"
              v-for="(item, index) in rankList"
              :key="index"
              class="el-row">
        <el-form-item
          :label="item.label"
        >
          <el-input v-model="item.value" disabled v-if="item.label=='等级名称'" />
          <el-input-number v-model="item.money" v-if="item.label=='金额'" controls-position="right" :min="0"
          />
        </el-form-item>
      </el-col>
    </el-row>
    <div  class="dialog-footer" align="center">
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-form>

</template>

<script>
  import {listRank, updateRank} from "@/api/scholarship/hardRank"

  export default {
    name: "hardRank",
    data() {
      return {
        rankList: [],
        form: {},
      };
    },
    created() {
      this.getRankList();
    },
    methods: {
      getRankList() {
        listRank().then(res => {
          let dataList = res.rows;
          let arrayList = []
          dataList.forEach((item, index) => {
            let obj = {
              id: index,
              label: '等级名称',
              value: item.rankName,
            }
            let money = {
              rangkId: item.rangkId,
              label: '金额',
              money: parseInt(item.money)
            }
            arrayList.push(obj)
            arrayList.push(money)
          })
          this.rankList = arrayList;
        })
      },
      cancel() {
        this.$emit('closeRank')
      },
      submitForm() {
        let updateList=[]
        this.rankList.forEach((item,index)=>{
          if(item.money !=undefined){
            updateList.push(item)
          }
        })
        updateRank(updateList).then(res=>{
          this.$modal.msgSuccess("修改成功")
          this.cancel();
        })
      }
    },
  }
</script>

<style scoped>
  .el-row{
    display:flex;
    flex-wrap: wrap;
  }

</style>
