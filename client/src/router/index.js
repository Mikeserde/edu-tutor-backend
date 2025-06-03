import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '主页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '主页', icon: 'dashboard' }
    }]
  },

  {
    path: '/teachers',
    component: Layout, // 使用Layout作为父组件
    redirect: '/teachers/list', // 可选的重定向
    name: 'TeacherManagement',
    meta: {
      title: '教师管理',
      roles: ['admin', 'editor'] // 根据需要添加角色限制
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/teacher/index'),
        name: 'TeacherList',
        meta: { title: '教师管理', icon: 'el-icon-user' }
      }
    ]
  },
  {
    path: '/students',
    component: Layout,
    redirect: '/students/list',
    name: 'StudentManagement',
    meta: {
      title: '学生管理',
      roles: ['admin', 'editor'] // 根据需要添加角色限制
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/student/index'),
        name: 'StudentList',
        meta: { title: '学生管理', icon: 'el-icon-user-solid' }
      }
    ]
  },
  {
    path: '/occupation-types',
    component: Layout,
    redirect: '/occupation-types/list',
    name: 'OccupationTypeManagement',
    meta: {
      title: '职业类型管理',
      roles: ['admin', 'editor'] // 根据需要添加角色限制
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/occupationType/index'),
        name: 'OccupationTypeList',
        meta: { title: '职业类型管理', icon: 'el-icon-tickets' }
      }
    ]
  },
  {
    path: '/occupation-registrations',
    component: Layout,
    redirect: '/occupation-registrations/list',
    name: 'OccupationRegistrationManagement',
    meta: {
      title: '职业注册管理',
      roles: ['admin', 'editor'] // 根据需要添加角色限制
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/occupationRegistration/index'),
        name: 'OccupationRegistrationList',
        meta: { title: '职业注册管理', icon: 'el-icon-notebook-2' }
      }
    ]
  },
  {
    path: '/occupation-schedules',
    component: Layout,
    redirect: '/occupation-schedules/list',
    name: 'OccupationScheduleManagement',
    meta: {
      title: '职业排班管理',
      icon: 'el-icon-date',
      roles: ['admin', 'editor'] // 根据需要添加角色限制
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/occupationSchedule/index'),
        name: 'OccupationScheduleList',
        meta: { title: '职业排班管理', icon: 'el-icon-date' }
      }
    ]
  },
  {
    path: '/salary',
    component: Layout,
    redirect: '/salary/list',
    name: 'Salary',
    meta: {
      title: '工资管理',
      roles: ['admin', 'editor']
    },
    children: [
      {
        path: 'list',
        component: () => import('@/views/salary/index'),
        name: 'SalaryList',
        meta: { title: '工资管理', icon: 'el-icon-money' }
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
