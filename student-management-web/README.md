# student-management-web

学生管理系统前端工程，基于 Vue 3 + Vite 构建，对接仓库中的既有 Spring Boot 后端，提供学生数据的查询、筛选、维护和详情查看能力。

## 项目定位

本项目是 `SCU` 仓库中的新增前端内容，主要目标是为已有学生管理后端补充完整的浏览器端管理界面，形成可演示、可联调、可继续扩展的前后端分离实践项目。

## 功能实现

当前页面入口为 `/students`，核心功能如下：

- 学生分页列表展示
- 按姓名进行模糊查询
- 按最小年龄、最大年龄进行区间筛选
- 新增学生
- 编辑学生
- 删除学生
- 查看学生详情
- 错误消息与异常结果提示
- 列表页统计信息展示

## 关键实现说明

### 1. 页面结构

页面以学生控制台为中心，包含以下区域：

- 顶部信息区
- 当前页统计卡片
- 左侧筛选面板
- 右侧数据表格
- 新增/编辑弹窗
- 学生详情抽屉

### 2. 状态管理

使用 `Pinia` 统一管理以下状态：

- 查询条件
- 分页信息
- 学生列表数据
- 详情数据
- 列表加载状态和详情加载状态

对应文件：

- [student.js](/d:/code/vuedemo/SCU/student-management-web/src/stores/student.js)

### 3. 请求封装

通过 `Axios` 创建统一的 HTTP 实例，并对响应进行拦截处理：

- 成功时直接返回后端响应中的 `data`
- 失败时统一抛出后端返回的错误对象
- 默认请求前缀为 `/api`

对应文件：

- [http.js](/d:/code/vuedemo/SCU/student-management-web/src/services/http.js)
- [student.js](/d:/code/vuedemo/SCU/student-management-web/src/services/student.js)

### 4. 与后端联调方式

本地开发时通过 Vite 代理将前端请求转发到后端：

- 前端请求前缀：`/api`
- 代理目标：`http://localhost:8888`

对应文件：

- [vite.config.js](/d:/code/vuedemo/SCU/student-management-web/vite.config.js)

## 技术栈

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Vue | 3.5.12 | 组件开发 |
| Vue Router | 4.4.5 | 路由管理 |
| Pinia | 2.2.4 | 状态管理 |
| Axios | 1.7.7 | HTTP 请求 |
| Element Plus | 2.8.4 | UI 组件库 |
| Vite | 5.4.10 | 构建工具 |
| @vitejs/plugin-vue | 5.1.4 | Vue 编译支持 |

## 目录结构

```text
student-management-web
├─ src
│  ├─ router
│  ├─ services
│  ├─ stores
│  ├─ views
│  ├─ App.vue
│  └─ main.js
├─ index.html
├─ package.json
└─ vite.config.js
```

## 本地运行

先确保后端服务已经启动，并监听 `http://localhost:8888`。

```bash
cd student-management-web
npm install
npm run dev
```

开发环境访问地址：

- 页面地址：`http://localhost:5173/students`
- 接口代理：`http://localhost:5173/api/*`

## 测试情况

截至 2026-03-30，已完成以下验证：

### 构建验证

- 执行 `npm run build` 成功

### 联调验证

依赖现有 `student-management-system` 后端服务，已完成以下手工联调：

- 学生列表查询
- 新增学生
- 查看学生详情
- 编辑学生
- 删除学生
- 删除后再次查询返回 404

### 当前测试边界

- 当前未集成 `Vitest`、`Playwright` 或 `Cypress`
- 本次前端新增内容的验证方式以构建通过和实际接口联调为主
