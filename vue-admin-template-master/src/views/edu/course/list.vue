<template>
    <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="courseQuery.title" placeholder="课程名"/>
      </el-form-item>
      <el-form-item>
        <el-select v-model="courseQuery.status" clearable placeholder="课程状态">
          <el-option :value="'Normal'" label="已发布"/>
          <el-option :value="'Draft'" label="未发布"/>
        </el-select>
      </el-form-item>
      <el-form-item label="添加时间">
        <el-date-picker
          v-model="courseQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="courseQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

        <!-- 表格 -->
        <el-table
        :data="list"
        element-loading-text="数据加载中"
        border
        fit
        highlight-current-row>
            <el-table-column
                label="序号"
                width="70"
                align="center">
                <template slot-scope="scope">
                {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" width="180" />
            <el-table-column prop="viewCount" label="浏览数量" width="80" />
            <el-table-column prop="lessonNum" label="课时数" width="80" />
            <el-table-column label="状态" width="90">
                <template slot-scope="scope">
                {{ scope.row.status === 'Normal'?'已发布':'未发布' }}
                </template>
            </el-table-column>
            <el-table-column prop="gmtCreate" label="添加时间" width="160"/>
            <el-table-column label="操作" width="420" align="center">
                <template slot-scope="scope">
                <router-link :to="'/edu/course/info/'+scope.row.id">
                    <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程基本信息</el-button>
                </router-link>
                <router-link :to="'/edu/course/chapter/'+scope.row.id">
                    <el-button type="primary" size="mini" icon="el-icon-edit">编辑课程大纲</el-button>
                </router-link>
                <el-button type="danger" size="mini" icon="el-icon-delete" @click="removeDataById(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

         <!-- 分页 -->
        <el-pagination 
            :current-page="page"
            :page-size="limit"
            :total="total"
            style="padding: 30px 0; text-align: center;"
            layout="total, prev, pager, next, jumper"
            @current-change="getList"
        />
        
        
    </div>
</template>

<script>
import { getCourseList,deleteCourse } from '@/api/edu/course'

export default {
  data(){
      return {
          list: null, //接口返回结果
          total: 0,
          page: 1 ,//当前页 
          limit: 6, //每页记录数
          courseQuery:{
          } //条件封装对象
      }
  },

  created(){ //页面渲染之前执行，一般调用methods定义的方法
        this.getList();
  },

  methods:{
      getList(page = 1){
          this.page = page
          getCourseList(this.page,this.limit,this.courseQuery)
            .then(
                response => {
                    this.total = response.data.total
                    this.list = response.data.rows
                    console.log(response)
                }
            ) //请求成功
            .catch(
                error => {
                    console.log(error)
                }
            ) //请求失败
      },
      resetData() { // 情况表单
          //表单输入项数据清空
          this.courseQuery = {}
          //查询所有讲师数据
          this.getList();
      },
      removeDataById(id){
        deleteCourse(id).then(response => {
          this.$message({
                  type: 'success',
                  message: '删除成功!'
          });
          this.getList();
        })
        .catch(
          error => {
              console.log(error)
          }
        ) //请求失败
      }
  }


}
</script>
