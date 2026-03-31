<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { storeToRefs } from 'pinia'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Delete,
  EditPen,
  Plus,
  Refresh,
  Search,
  User,
  View,
} from '@element-plus/icons-vue'
import { useStudentStore } from '@/stores/student'

const store = useStudentStore()
const {
  filters,
  pagination,
  students,
  loading,
  detailLoading,
  selectedStudent,
  currentPageCount,
  averageAge,
  ageRange,
} = storeToRefs(store)

const drawerVisible = ref(false)
const dialogVisible = ref(false)
const dialogMode = ref('create')
const submitLoading = ref(false)
const formRef = ref()

const form = reactive({
  id: undefined,
  name: '',
  age: 18,
})

const rules = {
  name: [
    { required: true, message: '请输入学生姓名', trigger: 'blur' },
    { min: 1, max: 20, message: '姓名长度需在 1 到 20 个字符之间', trigger: 'blur' },
  ],
  age: [
    { required: true, message: '请输入学生年龄', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value === '' || value === null || value === undefined) {
          callback(new Error('请输入学生年龄'))
          return
        }

        const num = Number(value)
        if (!Number.isInteger(num) || num < 0 || num > 150) {
          callback(new Error('年龄必须是 0 到 150 之间的整数'))
          return
        }

        callback()
      },
      trigger: 'blur',
    },
  ],
}

const statCards = computed(() => [
  {
    label: '学生总数',
    value: pagination.value.total,
    accent: 'sun',
    note: '由后端分页接口返回的总记录数',
  },
  {
    label: '当前页条目',
    value: currentPageCount.value,
    accent: 'sea',
    note: `第 ${pagination.value.current} 页，共 ${pagination.value.pages || 0} 页`,
  },
  {
    label: '平均年龄',
    value: averageAge.value,
    accent: 'ink',
    note: '基于当前页数据实时计算',
  },
  {
    label: '年龄范围',
    value: ageRange.value,
    accent: 'leaf',
    note: '当前页最小与最大年龄',
  },
])

const activeFilterCount = computed(() => {
  let count = 0
  if (filters.value.name) count += 1
  if (filters.value.minAge !== undefined) count += 1
  if (filters.value.maxAge !== undefined) count += 1
  return count
})

const dialogTitle = computed(() => (dialogMode.value === 'create' ? '新增学生' : '编辑学生'))
const drawerTitle = computed(() =>
  selectedStudent.value ? `学生详情 · ${selectedStudent.value.name}` : '学生详情',
)

function getErrorMessage(error) {
  if (error?.errors && typeof error.errors === 'object') {
    return Object.values(error.errors).join('；')
  }

  return error?.message || '操作失败'
}

function resetForm() {
  form.id = undefined
  form.name = ''
  form.age = 18
}

function openCreateDialog() {
  dialogMode.value = 'create'
  resetForm()
  dialogVisible.value = true
}

function openEditDialog(student) {
  dialogMode.value = 'edit'
  form.id = student.id
  form.name = student.name
  form.age = student.age
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!formRef.value) {
    return
  }

  try {
    await formRef.value.validate()
    submitLoading.value = true

    if (dialogMode.value === 'create') {
      await store.addStudent({
        name: form.name.trim(),
        age: Number(form.age),
      })
      ElMessage.success('新增学生成功')
    } else {
      await store.editStudent({
        id: form.id,
        name: form.name.trim(),
        age: Number(form.age),
      })
      ElMessage.success('修改学生成功')
    }

    dialogVisible.value = false
  } catch (error) {
    if (error !== false) {
      ElMessage.error(getErrorMessage(error))
    }
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(student) {
  try {
    await ElMessageBox.confirm(
      `确定删除学生“${student.name}”吗？该操作不可撤销。`,
      '删除确认',
      {
        type: 'warning',
        confirmButtonText: '删除',
        cancelButtonText: '取消',
      },
    )

    await store.removeStudent(student.id)
    ElMessage.success('删除学生成功')
  } catch (error) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(getErrorMessage(error))
    }
  }
}

async function openDetail(student) {
  drawerVisible.value = true
  try {
    await store.fetchStudentDetail(student.id)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

async function handleSearch() {
  if (
    filters.value.minAge !== undefined &&
    filters.value.maxAge !== undefined &&
    Number(filters.value.minAge) > Number(filters.value.maxAge)
  ) {
    ElMessage.warning('最小年龄不能大于最大年龄')
    return
  }

  store.setPage(1)
  try {
    await store.fetchStudents(1)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

async function handleReset() {
  store.resetFilters()
  try {
    await store.fetchStudents(1)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

async function handlePageChange(page) {
  store.setPage(page)
  try {
    await store.fetchStudents(page)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

async function handleSizeChange(size) {
  store.setPageSize(size)
  try {
    await store.fetchStudents(1)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

async function handleRefresh() {
  try {
    await store.refresh()
    ElMessage.success('列表已刷新')
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
}

onMounted(async () => {
  try {
    await store.fetchStudents(1)
  } catch (error) {
    ElMessage.error(getErrorMessage(error))
  }
})
</script>

<template>
  <div class="dashboard-page">
    <section class="masthead">
      <div class="masthead-copy">
        <div class="brand-row">
          <span class="brand-mark">SCU</span>
          <span class="brand-text">Student Management Console</span>
        </div>
        <h1>学生信息管理工作台</h1>
      </div>
      <div class="masthead-status">
        <span class="status-label">服务状态</span>
        <strong>Backend Ready</strong>
        <small>localhost:8888</small>
      </div>
    </section>

    <section class="stats-grid">
      <article
        v-for="card in statCards"
        :key="card.label"
        class="stat-card"
        :data-accent="card.accent"
      >
        <span class="stat-label">{{ card.label }}</span>
        <strong class="stat-value">{{ card.value }}</strong>
        <span class="stat-note">{{ card.note }}</span>
      </article>
    </section>

    <section class="workspace-grid">
      <aside class="control-card">
        <div class="panel-head">
          <div>
            <p class="section-tag">Filters</p>
            <h2>检索面板</h2>
          </div>
          <span class="filter-badge">{{ activeFilterCount }} 个筛选条件</span>
        </div>

        <el-form class="filter-stack" :inline="false">
          <el-form-item label="姓名检索">
            <el-input
              v-model="filters.name"
              clearable
              placeholder="按姓名模糊查询"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="最小年龄">
            <el-input-number
              v-model="filters.minAge"
              :min="0"
              :max="150"
              controls-position="right"
              placeholder="最小年龄"
            />
          </el-form-item>
          <el-form-item label="最大年龄">
            <el-input-number
              v-model="filters.maxAge"
              :min="0"
              :max="150"
              controls-position="right"
              placeholder="最大年龄"
            />
          </el-form-item>
          <el-form-item class="stack-actions">
            <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </aside>

      <section class="board-card">
        <div class="panel-head board-head">
          <div>
            <p class="section-tag">Roster</p>
            <h2>学生数据表</h2>
          </div>
          <div class="toolbar-actions">
            <el-button :icon="Refresh" plain @click="handleRefresh">刷新</el-button>
            <el-button type="primary" :icon="Plus" @click="openCreateDialog">新增学生</el-button>
          </div>
        </div>

        <div class="table-frame">
          <el-table
            :data="students"
            v-loading="loading"
            class="student-table"
            empty-text="暂无学生数据"
          >
            <el-table-column prop="id" label="ID" min-width="160" />
            <el-table-column prop="name" label="姓名" min-width="180" />
            <el-table-column prop="age" label="年龄" min-width="120" />
            <el-table-column label="操作" min-width="260" fixed="right">
              <template #default="{ row }">
                <div class="table-actions">
                  <el-button link type="primary" :icon="View" @click="openDetail(row)">详情</el-button>
                  <el-button link type="warning" :icon="EditPen" @click="openEditDialog(row)">编辑</el-button>
                  <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrap">
          <span class="pagination-note">共 {{ pagination.total }} 条记录</span>
          <el-pagination
            background
            layout="total, sizes, prev, pager, next"
            :current-page="pagination.current"
            :page-size="pagination.size"
            :page-sizes="[5, 10, 20, 50]"
            :total="pagination.total"
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </div>
      </section>
    </section>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="480px"
      destroy-on-close
      @closed="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item v-if="dialogMode === 'edit'" label="学生 ID">
          <el-input :model-value="form.id" disabled />
        </el-form-item>
        <el-form-item label="学生姓名" prop="name">
          <el-input v-model="form.name" maxlength="20" show-word-limit placeholder="请输入学生姓名" />
        </el-form-item>
        <el-form-item label="学生年龄" prop="age">
          <el-input-number v-model="form.age" :min="0" :max="150" class="full-width" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-actions">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ dialogMode === 'create' ? '确认新增' : '保存修改' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer v-model="drawerVisible" :title="drawerTitle" size="420px">
      <div v-loading="detailLoading" class="detail-panel">
        <template v-if="selectedStudent">
          <div class="detail-hero">
            <div class="avatar-chip">
              <el-icon><User /></el-icon>
            </div>
            <div>
              <h3>{{ selectedStudent.name }}</h3>
              <p>学生档案已从后端实时拉取</p>
            </div>
          </div>

          <div class="detail-grid">
            <div class="detail-item">
              <span>学生 ID</span>
              <strong>{{ selectedStudent.id }}</strong>
            </div>
            <div class="detail-item">
              <span>姓名</span>
              <strong>{{ selectedStudent.name }}</strong>
            </div>
            <div class="detail-item">
              <span>年龄</span>
              <strong>{{ selectedStudent.age }}</strong>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无详情数据" />
      </div>
    </el-drawer>
  </div>
</template>
