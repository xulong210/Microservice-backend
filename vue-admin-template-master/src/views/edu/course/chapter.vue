<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>
    <el-steps :active="2" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息"/>
      <el-step title="创建课程大纲"/>
      <el-step title="发布"/>
    </el-steps>

    <el-button type="text" @click="openDialog">添加章节</el-button>

    <ul class="chanpterList">
        <li
            v-for="chapter in chapterVideoList"
            :key="chapter.id">
            <p>
                {{ chapter.title }}
                <span class="acts">
                    <el-button type="text" @click="openVideo(chapter.id)">添加小节</el-button>
                    <el-button type="text" @click="openEditChapter(chapter.id)">编辑</el-button>
                    <el-button type="text" @click="removeChapter(chapter.id)">删除</el-button>
                </span>
            </p>

            <!-- 视频 -->
            <ul class="chanpterList videoList">
                <li
                    v-for="video in chapter.children"
                    :key="video.id">
                    <p>{{ video.title }}
                        <span class="acts">
                            <el-button type="text" @click="getVideoInfo(video.id)">编辑</el-button>
                            <el-button type="text" @click="removeVideo(video.id)">删除</el-button>
                        </span>
                    </p>
                </li>
            </ul>
        </li>
    </ul>

    <div>
        <el-button @click="previous">上一步</el-button>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
        <el-form :model="chapter" label-width="120px">
            <el-form-item label="章节标题">
                <el-input v-model="chapter.title"/>
            </el-form-item>

            <el-form-item label="章节排序">
                <el-input-number v-model="chapter.sort" :min="0" controls-position="right"/>
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
        </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">

      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title"/>
        </el-form-item>

        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right"/>
        </el-form-item>

        <el-form-item label="是否免费">
          <el-radio-group v-model="video.free">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">默认</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="上传视频">
            <el-upload
                  :on-success="handleVodUploadSuccess"
                  :on-remove="handleVodRemove"
                  :before-remove="beforeVodRemove"
                  :on-exceed="handleUploadExceed"
                  :file-list="fileList"
                  :action="BASE_API+'/eduvideo/video/upload'"
                  :limit="1"
                  class="upload-demo">
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
                <div slot="content">最大支持1G,<br>
                    支持3GP、ASF、AVI、DAT、DV、FLV、F4V、<br>
                    GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、<br>
                    MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、<br>
                    SWF、TS、VOB、WMV、WEBM 等视频格式上传</div>
                <i class="el-icon-question"/>
            </el-tooltip>
            </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>
<script>
import { saveVideo,getVideoInfo,updateVideo,deleteVideo,deleteAliyunVideo } from '@/api/edu/video'
import { getAllChapterVideo,addChapter,getChapterInfo,updateChapter,deleteChapter } from '@/api/edu/chapter'
export default {
  data() {
    return {
      saveBtnDisabled: false, // 保存按钮是否禁用
      chapterVideoList: [],
      courseId:0,
      dialogChapterFormVisible: false, //章节弹框值
      dialogVideoFormVisible: false, //小节弹框值
      chapter: { //封装章节的数据
        title: '',
        sort: 0,
        courseId: 0,
      },
      video: {
        title: '',
        sort: 0,
        free: 0,
        videoSourceId: '',
        videoOriginalName: '',
      },
      fileList: [],//上传文件列表
      BASE_API: process.env.BASE_API // 接口API地址
    }
  },

  created() {
    //获取路由id值
    if(this.$route.params && this.$route.params.id){
      this.courseId = this.$route.params.id
    }
    this.getChapterVideoList();
  },

  methods: {

    //===========================================小节操作
    //上传视频成功调用的方法
    handleVodUploadSuccess(response,file,fileList){
        this.video.videoSourceId = response.data.videoId
        this.video.videoOriginalName = file.name
    },
    
    handleUploadExceed(){
        this.$message.warning("想要重新上传,请先删除已上传的视频")
    },
    //删除前弹出提示框(点击x调用这个方法)
    beforeVodRemove(file,fileList){
        return this.$confirm(`确定移除 ${file.name}?`);
    },
    //处理删除视频逻辑(点击删除提示框的确定方法)
    handleVodRemove(file,fileList){
        //调用接口的删除视频方法。
        deleteAliyunVideo(this.video.videoSourceId)
          .then(response => {
              this.$message({
                  type: 'success',
                  message: '删除成功!'
              });
              this.fileList = [];
              //删除视频相关信息 不然会加入数据库 
              this.videoOriginalName = '';
              this.videoSourceId = '';
          })
    },
    //删除小节
    removeVideo(videoId){
        this.$confirm('此操作将永久删除小节记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {  //点击确定，执行then方法
                //调用删除的方法
                deleteVideo(videoId)
                    .then(response =>{//删除成功
                    //提示信息
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    //回到列表页面
                    this.getChapterVideoList() 
                })
            })
    },

    //打开小节添加对话框
    openVideo(chapterId){
      this.dialogVideoFormVisible = true;
      //清空表单数据
      this.video.title = ''
      this.video.sort = 0
      this.video.free = 0
      this.video.videoSourceId = ''
      this.fileList = []

      //设置章节id
      this.video.chapterId = chapterId;
    },

    //添加小节
    addVideo(){
        //设置课程id
        this.video.courseId = this.courseId
        saveVideo(this.video)
          .then(response => {
            //关闭弹框
            this.dialogVideoFormVisible = false;
            this.$message({
              type: 'success',
              message: '添加小节成功'
            })

            //刷新页面
            this.getChapterVideoList();
          })
    },

    //修改小节弹框回显数据
    getVideoInfo(videoId){
        this.dialogVideoFormVisible = true
        getVideoInfo(videoId)
          .then(response => {
            this.video = response.data.video
            console.log(response.data)
          })

    },

    //修改小节
    editVideo(){
        updateVideo(this.video)
          .then(response => {
            //关闭弹框
            this.dialogVideoFormVisible = false;
            this.$message({
              type: 'success',
              message: '修改小节成功'
            })

            //刷新页面
           this.getChapterVideoList();
          })
    },

    saveOrUpdateVideo(){
        if(!this.video.id){
          this.addVideo()
        }else{
          this.editVideo()
        }
    },

    //===============================================章节操作
    //打开添加对话框
    openDialog(){
      this.dialogChapterFormVisible = true
      this.chapter.title = ''
      this.chapter.sort = 0
    },

    //添加章节信息
    saveChapter(){
      this.chapter.courseId = this.courseId
      addChapter(this.chapter)
        .then(response => {
          //关闭弹框
          this.dialogChapterFormVisible = false;
          this.$message({
            type: 'success',
            message: '添加章节成功'
          })

          //刷新页面
          this.getChapterVideoList();
        })
    },

    //修改章节信息弹框 回显数据
    openEditChapter(chapterId){
        this.dialogChapterFormVisible = true
        getChapterInfo(chapterId)
          .then(response => {
            this.chapter = response.data.chapter
          })
    },

    //修改章节信息
    editChapter(){
        updateChapter(this.chapter)
          .then(response => {
            //关闭弹框
            this.dialogChapterFormVisible = false;
            this.$message({
              type: 'success',
              message: '修改章节成功'
            })

            //刷新页面
           this.getChapterVideoList();
          })
    },

    //根据条件判断是修改还是添加
    saveOrUpdate(){
      if(!this.chapter.id){
        this.saveChapter()
      }else{
        this.editChapter()
      }
    
    },

    //根据课程id 查询所有章节和小节
    getChapterVideoList(){
      getAllChapterVideo(this.courseId)
        .then(response => {
          this.chapterVideoList = response.data.list;
        })
    },

    //删除章节
    removeChapter(chapterId){
        this.$confirm('此操作将永久删除章节记录, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {  //点击确定，执行then方法
                //调用删除的方法
                deleteChapter(chapterId)
                    .then(response =>{//删除成功
                    //提示信息
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                    //回到列表页面
                    this.getChapterVideoList() 
                })
            }) //点击取消，执行catch方法
    },

    previous() {
      this.$router.push({ path: '/edu/course/info/'+this.courseId})
    },

    next() {
      this.$router.push({ path: '/edu/course/publish/'+this.courseId})
    },

  }

}

</script>

<style scoped>

.chanpterList{

    position: relative;
    list-style: none;
    margin: 0;
    padding: 0;

}

.chanpterList li{
  position: relative;

}

.chanpterList p{

  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #DDD;

}

.chanpterList .acts {
    float: right;
    font-size: 14px;

}

.videoList{

  padding-left: 50px;

}

.videoList p{
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #DDD;

}

</style>