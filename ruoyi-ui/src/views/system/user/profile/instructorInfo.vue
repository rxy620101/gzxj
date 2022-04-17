<template>
  <el-form ref="form" :model="instructor" :rules="rules" label-width="100px">
    <el-form-item label="联系电话" prop="tel">
      <el-input v-model="instructor.tel" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="instructor.email" maxlength="50" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateInfo } from "@/api/scholarship/instrorInfo";

export default {
  props: {
    instructor: {
      type: Object
    }
  },
  data() {
    return {
      // 表单校验
      rules: {
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        tel: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateInfo(this.instructor).then(response => {
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
