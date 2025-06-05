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
    redirect: '/home',
    children: [{
      path: 'home',
      name: 'home',
      component: () => import('@/views/home/index'),
      meta: {
        title: '主页',
        icon: 'el-icon-s-home',
      }
    }]
  },

  {
    path: '/teachers',
    component: Layout, // 使用Layout作为父组件
    redirect: '/teachers/list', // 可选的重定向
    name: 'TeacherManagement',
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
    children: [
      {
        path: 'list',
        component: () => import('@/views/salary/index'),
        name: 'SalaryList',
        meta: { title: '工资管理', icon: 'el-icon-money' }
      }
    ]
  },
  {
    path: '/payments',
    component: Layout,
    redirect: '/payments/list',
    name: 'Payments',
    children: [
      {
        path: 'list',
        component: () => import('@/views/payment/index'),
        name: 'PaymentList',
        meta: { title: '支付记录管理', icon: 'el-icon-wallet' }
      }
    ]
  },
  {
    path: '/reports',
    component: Layout,
    redirect: '/reports/teacher-hours',
    name: 'Reports',
    meta: {
      title: '统计报表',
      icon: 'el-icon-s-data'
    },
    children: [
      {
        path: 'teacher-hours',
        component: () => import('@/views/report/TeacherHours'),
        name: 'TeacherHoursReport',
        meta: {
          title: '教师工时统计',
          icon: 'el-icon-time'
        }
      },
      {
        path: 'occupation-demand',
        component: () => import('@/views/report/OccupationDemand'),
        name: 'OccupationDemandReport',
        meta: {
          title: '职业需求统计',
          icon: 'el-icon-s-marketing'
        }
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
