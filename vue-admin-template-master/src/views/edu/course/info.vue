<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="1" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布"/>
    </el-steps>
    
    <el-form label-width="120px">
        <el-form-item label="课程标题">
            <el-input v-model="courseInfo.title" placeholder=" 示例:机器学习项目课 从基础到搭建项目视频课程。专业名称注意大小写"/>
        </el-form-item>

        <!-- 所属分类-->
        <el-form-item label="课程分类">
            <el-select
                v-model="courseInfo.subjectParentId"
                placeholder="一级分类" 
                @change="subjectChange">
                <el-option
                    v-for="oneSubject in oneSubjects"
                    :key="oneSubject.id"
                    :label="oneSubject.title"
                    :value="oneSubject.id"/>
            </el-select>

           <el-select
                v-model="courseInfo.subjectId"
                placeholder="二级分类">
                <el-option
                    v-for="twoSubject in twoSubjects"
                    :key="twoSubject.id"
                    :label="twoSubject.title"
                    :value="twoSubject.id"/>
            </el-select> 
        </el-form-item>

        <!-- 课程讲师-->
        <el-form-item label="课程讲师">
            <el-select
                v-model="courseInfo.teacherId"
                placeholder="教师选择">
                <el-option
                    v-for="teacher in teacherList"
                    :key="teacher.id"
                    :label="teacher.name"
                    :value="teacher.id"/>
            </el-select>
        </el-form-item>

        <el-form-item label="总课时">
            <el-input-number :min="0" v-model="courseInfo.lessonNum" controls-position="right" placeholder="请填写课程的总课时数"/>
        </el-form-item>

        <!-- 课程简介 -->
        <el-form-item label="课程简介">
            <tinymce :height="300" v-model="courseInfo.description"/>
        </el-form-item>

        <!-- 课程封面 -->
        <el-form-item label="课程封面">
            <el-upload
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :action="BASE_API+'/eduoss/fileoss/upload'"
                class="avatar-uploader">
                <img :src="courseInfo.cover">
            </el-upload>
        </el-form-item>

        <el-form-item label="课程价格">
            <el-input-number :min="0" v-model="courseInfo.price" controls-position="right" placeholder="免费课程请设置为0元"/> 元
        </el-form-item>

        <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
        </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { addCourseInfo,getListTeacher,getCourseInfoById,updateCourseInfo } from '@/api/edu/course'
import { getSubjectList } from '@/api/edu/subject'
import Tinymce from '@/components/Tinymce'
export default {

  components: {Tinymce},
 
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      courseInfo: {
          title: '',
          subjectId: '',
          subjectParentId: '',
          teacherId: '',
          lessonNum: 0,
          description: '',
          cover: '/static/01.jpg',
          price: 0  
      },
      BASE_API: process.env.BASE_API,
      teacherList:[],
      oneSubjects:[],
      twoSubjects:[],
      courseId: 0
    }
  },
  created() {
      //获取路由中的id值
      if(this.$route.params && this.$route.params.id){
          this.courseId = this.$route.params.id
          this.getInfo()
      } else{
          this.getAllTeacher()
          this.getAllSubjects()
      }
  },
  methods: {
    //添加课程
    addCourse(){
        addCourseInfo(this.courseInfo)
        .then(response => {
          this.$message({
              type: 'success',
              message: '添加课程信息成功'
          })
          this.$router.push({ path: '/edu/course/chapter/'+response.data.courseId })

        })
    },
    //修改课程
    updateCourse(){
        updateCourseInfo(this.courseInfo)
            .then(response => {
                this.$message({
                type: 'success',
                message: '修改课程信息成功'
            })
            this.$router.push({ path: '/edu/course/chapter/'+this.courseId })
            })
    },

    //根据id选择是添加还是修改
    saveOrUpdate() {
        if(!this.courseInfo.id){
            this.addCourse();
        }else {
            this.updateCourse();
        }
    },
    getInfo(){
        getCourseInfoById(this.courseId)
            .then(response => {
                this.courseInfo = response.data.courseInfoVo
                console.log(123);
                //1.查询所有的分类
                getSubjectList()
                    .then(response => {
                        this.oneSubjects = response.data.list

                        //2.遍历一级分类 匹配和当前一级分类id一样的数据
                        for(var i=0;i<this.oneSubjects.length;i++){
                            var oneSubject = this.oneSubjects[i];
                            if(this.courseInfo.subjectParentId == oneSubject.id){
                                this.twoSubjects = oneSubject.children
                            }
                        }
                    })
                //2.查询所有教师信息
                this.getAllTeacher();
            })
    },

    getAllTeacher(){
        getListTeacher()
            .then(response => {
                this.teacherList = response.data.list;
            })
    },
    getAllSubjects(){
        getSubjectList()
            .then(response => {
                this.oneSubjects = response.data.list;
            })
    },
    //点击一级分类触发的事件 展示对应的二级分类
    subjectChange(value){
        for(let i = 0;i < this.oneSubjects.length;i++){
            if(this.oneSubjects[i].id == value){
                this.twoSubjects = this.oneSubjects[i].children
                this.courseInfo.subjectId = '';
            }
        }
    },

    //上传成功
    handleAvatarSuccess(res,file){
        this.courseInfo.cover = res.data.url
    },

    //上传之前调用的方法
   beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg'
        const isLt2M = file.size / 1024 / 1024 < 2
        if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!')
        }
        if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!')
        }
        return isJPG && isLt2M
    }
  }
}
</script>


<style scoped>
    .tinymce-container {
        line-height: 29px;
    }
</style>