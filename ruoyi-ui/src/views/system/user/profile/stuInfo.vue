<template>
  <el-form ref="form" :model="stu" :rules="rules" label-width="100px">
    <el-form-item label="身份证号" prop="certNum">
      <el-input v-model="stu.certNum" maxlength="11" disabled />
    </el-form-item>
    <el-form-item label="联系电话" prop="tel">
      <el-input v-model="stu.tel" maxlength="11" />
    </el-form-item>
    <el-form-item label="家庭联系人" prop="guidName">
      <el-input v-model="stu.guidName" maxlength="20" />
    </el-form-item>
    <el-form-item label="联系人电话" prop="guidmanTel">
      <el-input v-model="stu.guidmanTel" maxlength="11" />
    </el-form-item>
    <el-form-item label="家庭地址" prop="addr">
      <el-input v-model="stu.addr" maxlength="200" />
    </el-form-item>
    <el-form-item label="卡号" prop="cardNum">
      <el-input v-model="stu.cardNum" maxlength="19" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateInfo } from "@/api/scholarship/stuInfo";

export default {
  props: {
    stu: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        tel: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        guidName: [
          { required: true, message: "联系人不能为空", trigger: "blur" },
          {max:20,message:"名字最长为20个字符",trigger:"blur"}
        ],
        guidmanTel: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        addr:[
          { required: true, message: "地址不能为空", trigger: "blur" },
        ],
        cardNum:[
          {
            validator: (rule, value, callback) => {
              if(value !=null){
                if(value.length !=19){
                  callback(new error("卡号的长度为19位！"))
                }
                else{
                  callback()
                }
              }
              else{
                callback()
              }

            }
          }
          ]

      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateInfo(this.stu).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.$forceUpdate()
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
