import { computed, reactive, ref } from 'vue'
import { defineStore } from 'pinia'
import {
  createStudent,
  deleteStudentById,
  getStudentById,
  getStudentPage,
  updateStudent,
} from '@/services/student'

function parseNumber(value) {
  if (value === '' || value === null || value === undefined) {
    return undefined
  }

  return Number(value)
}

export const useStudentStore = defineStore('student', () => {
  const filters = reactive({
    name: '',
    minAge: undefined,
    maxAge: undefined,
  })

  const pagination = reactive({
    current: 1,
    size: 10,
    total: 0,
    pages: 0,
  })

  const students = ref([])
  const loading = ref(false)
  const detailLoading = ref(false)
  const selectedStudent = ref(null)

  const currentPageCount = computed(() => students.value.length)
  const averageAge = computed(() => {
    if (!students.value.length) {
      return 0
    }

    const totalAge = students.value.reduce((sum, student) => sum + Number(student.age || 0), 0)
    return Number((totalAge / students.value.length).toFixed(1))
  })

  const ageRange = computed(() => {
    if (!students.value.length) {
      return '暂无数据'
    }

    const ages = students.value.map((student) => Number(student.age || 0))
    return `${Math.min(...ages)} - ${Math.max(...ages)}`
  })

  async function fetchStudents(page = pagination.current) {
    loading.value = true
    try {
      const data = await getStudentPage({
        current: page,
        size: pagination.size,
        name: filters.name || undefined,
        minAge: parseNumber(filters.minAge),
        maxAge: parseNumber(filters.maxAge),
      })

      students.value = data.records || []
      pagination.current = data.current || page
      pagination.size = data.size || pagination.size
      pagination.total = data.total || 0
      pagination.pages = data.pages || 0
      return data
    } finally {
      loading.value = false
    }
  }

  async function refresh() {
    return fetchStudents(pagination.current)
  }

  async function fetchStudentDetail(id) {
    detailLoading.value = true
    try {
      const data = await getStudentById(id)
      selectedStudent.value = data
      return data
    } finally {
      detailLoading.value = false
    }
  }

  async function addStudent(payload) {
    await createStudent(payload)
    if (pagination.current !== 1) {
      pagination.current = 1
    }
    await fetchStudents(pagination.current)
  }

  async function editStudent(payload) {
    await updateStudent(payload)
    await fetchStudents(pagination.current)
    if (selectedStudent.value?.id === payload.id) {
      selectedStudent.value = { ...selectedStudent.value, ...payload }
    }
  }

  async function removeStudent(id) {
    await deleteStudentById(id)
    const lastItemOnPage = students.value.length === 1
    if (lastItemOnPage && pagination.current > 1) {
      pagination.current -= 1
    }
    await fetchStudents(pagination.current)
    if (selectedStudent.value?.id === id) {
      selectedStudent.value = null
    }
  }

  function resetFilters() {
    filters.name = ''
    filters.minAge = undefined
    filters.maxAge = undefined
    pagination.current = 1
  }

  function setPage(current) {
    pagination.current = current
  }

  function setPageSize(size) {
    pagination.size = size
    pagination.current = 1
  }

  return {
    filters,
    pagination,
    students,
    loading,
    detailLoading,
    selectedStudent,
    currentPageCount,
    averageAge,
    ageRange,
    fetchStudents,
    refresh,
    fetchStudentDetail,
    addStudent,
    editStudent,
    removeStudent,
    resetFilters,
    setPage,
    setPageSize,
  }
})
