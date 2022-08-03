import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },

  // {
  //   path: '/teacher',
  //   component: Layout,
  //   redirect: '/teacher/table',
  //   name: '教师管理',
  //   meta: { title: '教师管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'table',
  //       name: '教师列表',
  //       component: () => import('@/views/edu/teacher/list'),
  //       meta: { title: '教师列表', icon: 'table' }
  //     },
  //     {
  //       path: 'save',
  //       name: '添加教师',
  //       component: () => import('@/views/edu/teacher/save'),
  //       meta: { title: '添加教师', icon: 'tree' }
  //     },
  //     {
  //       path: 'edit/:id',
  //       name: 'EduTeacherEdit',
  //       component: () => import('@/views/edu/teacher/save'),
  //       meta: { title: '编辑教师', icon: 'tree' },
  //       hidden: true
  //     }
  //   ]
  // },

  // {
  //   path: '/subject',
  //   component: Layout,
  //   redirect: '/subject/list',
  //   name: '课程分类管理',
  //   meta: { title: '课程分类管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'list',
  //       name: '课程分类列表',
  //       component: () => import('@/views/edu/subject/list'),
  //       meta: { title: '课程分类列表', icon: 'table' }
  //     },
  //     {
  //       path: 'save',
  //       name: '添加课程分类',
  //       component: () => import('@/views/edu/subject/save'),
  //       meta: { title: '添加课程分类', icon: 'table' }
  //     },
  //   ]
  // },

  // {
  //   path: '/course',
  //   component: Layout,
  //   redirect: '/course/list',
  //   name: '课程管理',
  //   meta: { title: '课程管理', icon: 'example' },
  //   children: [
  //     {
  //       path: 'list',
  //       name: '课程列表',
  //       component: () => import('@/views/edu/course/list'),
  //       meta: { title: '课程列表', icon: 'table' }
  //     },
  //     {
  //       path: 'info',
  //       name: '编辑课程信息',
  //       component: () => import('@/views/edu/course/info'),
  //       meta: { title: '编辑课程信息', icon: 'table' }
  //     },
  //     {
  //       path: 'info/:id',
  //       name: '编辑课程信息',
  //       component: () => import('@/views/edu/course/info'),
  //       meta: { title: '编辑课程信息', icon: 'table' },
  //       hidden: true
  //     },
  //     {
  //       path: 'chapter/:id',
  //       name: '编辑课程大纲',
  //       component: () => import('@/views/edu/course/chapter'),
  //       meta: { title: '编辑课程大纲', icon: 'table' },
  //       hidden: true
  //     },
  //     {
  //       path: 'publish/:id',
  //       name: '发布课程',
  //       component: () => import('@/views/edu/course/publish'),
  //       meta: { title: '发布课程', icon: 'table' },
  //       hidden: true
  //     },
  //   ]
  // },

  // {
  //   path: '/sta',
  //   component: Layout,
  //   redirect: '/sta/table',
  //   name: '统计分析',
  //   meta: { title: '统计分析', icon: 'example' },
  //   children: [
  //     {
  //       path: 'create',
  //       name: '生成数据',
  //       component: () => import('@/views/sta/create'),
  //       meta: { title: '生成数据', icon: 'table' }
  //     },
  //     {
  //       path: 'show',
  //       name: '图表显示',
  //       component: () => import('@/views/sta/show'),
  //       meta: { title: '图表显示', icon: 'tree' }
  //     }
  //   ]
  // },

  // { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  // 发生路由切换以后让页面回到顶部
  scrollBehavior: () => ({ y: 0 }),
  //用来配置多个route对象
  routes: constantRoutes
})

// 创建一个路由器实例
const router = createRouter()

// export default new Router({
//   // mode: 'history', //后端支持可开
//   scrollBehavior: () => ({ y: 0 }),
//   routes: constantRouterMap
// })

export default router