<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center" style="height: 115px">
              <userAvatar :user="user"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />姓名
                <div class="pull-right" v-if="stu !=null">{{ stu.stuName }}</div>
                <div class="pull-right" v-if="instructor !=null">{{ instructor.instructorName }}</div>
              </li>
              <li class="list-group-item" >
                <svg-icon icon-class="sex" />性别
                <div class="pull-right" v-if="instructor !=null">
                  <dict-tag :options="dict.type.sys_user_sex" :value="instructor.sex"/>
                </div>
                <div class="pull-right" v-if="stu !=null">
                  <dict-tag :options="dict.type.sys_user_sex" :value="stu.sex"/>
                </div>
              </li>
              <li class="list-group-item" v-if="stu !=null">
                <svg-icon icon-class="sno" />学号
                <div class="pull-right">{{ stu.sno }}</div>
                <div class="pull-right" v-if="instructor !=null">{{ instructor.instructorName }}</div>
              </li>
              <li class="list-group-item" v-if="instructor != null">
                <svg-icon icon-class="sno" />职工号
                <div class="pull-right">{{ instructor.instructorId }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" />联系方式
                <div class="pull-right" v-if="stu !=null">{{ stu.tel }}</div>
                <div class="pull-right" v-if="instructor !=null">{{ instructor.tel }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="college" />所属学院
                <div class="pull-right" v-if="stu !=null">{{ stu.collegeName }}</div>
                <div class="pull-right" v-if="instructor !=null">{{ instructor.collegeName }}</div>
              </li>
              <li class="list-group-item" v-if="stu !=null">
                <svg-icon icon-class="major" />专业
                <div class="pull-right">{{ stu.majorName}}</div>
              </li>
              <li class="list-group-item" v-if="stu !=null">
                <svg-icon icon-class="grade" />年级
                <div class="pull-right">{{ stu.grade}}级</div>
              </li>
              <li class="list-group-item" v-if="stu !=null">
                <svg-icon icon-class="stuClass" />班级
                <div class="pull-right">{{ stu.stuClass}}</div>
              </li>
              <li class="list-group-item" v-if="stu !=null">
                <svg-icon icon-class="date" />入学时间
                <div class="pull-right">{{ stu.registerTime }}</div>
              </li>
              <li class="list-group-item" v-if="instructor !=null">
                <svg-icon icon-class="grade" />指导年级
                <div class="pull-right">{{ instructor.guideGrade }}</div>
              </li>
              <li class="list-group-item" v-if="instructor !=null">
                <svg-icon icon-class="major" />指导专业
                <div class="pull-right">{{guideMajorNames}}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <stuInfo :stu="stu" v-if="stu !=null"/>
              <instructorInfo :instructor="instructor" v-if="instructor !=null"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import stuInfo from "./stuInfo";
import instructorInfo from "./instructorInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";
import {selMajorNames} from "@/api/system/dept";

export default {
  name: "Profile",
  components: { userAvatar, stuInfo, resetPwd,instructorInfo },
  dicts: ['sys_user_sex'],
  data() {
    return {
      user: {},
      stu:{},
      instructor:{},
      activeTab: "userinfo",
      guideMajorNames:undefined,
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.stu = response.stu;
        this.instructor = response.instructor;
        if(this.instructor != null){
          let majorIds = this.instructor.guideMajorIds.split(",")
          selMajorNames(majorIds).then(res => {
            this.guideMajorNames = res.data.join(",")
          });
        }
      });
    }
  },
};
</script>
