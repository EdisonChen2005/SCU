import { createRouter, createWebHistory } from 'vue-router'
import StudentDashboard from '@/views/StudentDashboard.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/students',
    },
    {
      path: '/students',
      name: 'students',
      component: StudentDashboard,
    },
  ],
})

export default router
