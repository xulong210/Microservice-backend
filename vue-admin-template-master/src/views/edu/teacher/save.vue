<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name"/>
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number :min="0" v-model="teacher.sort" controls-position="right" />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师"/>
          <el-option :value="2" label="首席讲师"/>
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career"/>
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea"/>
      </el-form-item>
      <!-- 讲师头像：TODO -->
      <el-form-item label="讲师头像">
          <!-- 头衔缩略图 -->
          <pan-thumb :image="teacher.avatar"/>
          <!-- 文件上传按钮 -->
          <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像
          </el-button>
          <!--
      v-show：是否显示上传组件
      :key：类似于id，如果一个页面多个图片上传控件，可以做区分
      :url：后台上传的url地址
      @close：关闭上传组件
      @crop-upload-success：上传成功后的回调 -->
          <image-cropper
                        v-show="imagecropperShow"
                        :width="300"
                        :height="300"
                        :key="imagecropperKey"
                        :url="BASE_API+'/eduoss/fileoss/upload'"
                        field="file"
                        @close="close"
                        @crop-upload-success="cropSuccess"/>
      </el-form-item>
      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>



<script>

import { saveTeacher,getTeacherInfo,updateTeacher } from '@/api/edu/teacher'
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'

export default {
    components: { ImageCropper, PanThumb },
    data(){
        return {
            teacher:{
                name: '',
                sort: 0,
                level: 1,
                career: '',
                intro: '',
                avatar: ''        
            },
            //上传弹框组件是否显示
            imagecropperShow: false,
            imagecropperKey: 0,
            BASE_API: process.env.BASE_API,

            saveBtnDisabled: false,//保存按钮禁用
        }
    },
    created() {
      this.init()
    },
    watch: {
      $route(to,from) {
        this.init()
      }
    },
    methods:{
        init(){
          if(this.$route.params && this.$route.params.id){
            const id = this.$route.params.id
            this.getInfo(id)
          }else {
            //清空表单
            this.teacher = {}
          }
        },
        saveOrUpdate(){
          if(this.teacher.id){ 
            //修改
            this.editTeacher()
          }else{
          //添加
            this.addTeacher()
          }
        },

        //添加讲师的方法
        addTeacher(){
            saveTeacher(this.teacher)
                .then(response => {
                    //提示成功
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    //回到列表页面
                    this.$router.push({path:'/edu/teacher/list'})
                })
                .catch(error => {
                    console.log(error)
                })
        },

        //修改讲师信息回显
        getInfo(id){
            getTeacherInfo(id)
                .then(response => {
                    console.log(response.data.teacher)
                    this.teacher = response.data.teacher
                })
                .catch(error => {
                    console.log(error)
                })
        },
        
        //修改
        editTeacher(){
            updateTeacher(this.teacher)
                .then(response => {
                    //提示成功
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    //回到列表页面
                    this.$router.push({path:'/edu/teacher/list'})
                })
                .catch(error => {
                    console.log(error)
                })
        },

        //关闭上传弹框方法
        close(){
          this.imagecropperShow = false
          this.imagecropperKey = this.imagecropperKey+1
        },

        //图像上传成功
        cropSuccess(data){
          this.imagecropperShow = false
          //上传之后的接口返回图片地址
          this.teacher.avatar = data.url
        }
    }


}
</script>
