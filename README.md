# SCU

四川大学计算机专业个人项目集合仓库，用于沉淀课程实践、全栈开发练习和可展示的工程化作品。

## 仓库概览

当前仓库包含一个已有后端项目，以及一个新增的 Vue 3 前端项目：

| 子项目 | 说明 | 技术栈 |
| --- | --- | --- |
| [student-management-system](./student-management-system) | 学生管理后端服务，提供 CRUD、分页查询和异常处理能力 | Spring Boot、MyBatis-Plus、PostgreSQL |
| [student-management-web](./student-management-web) | 新增的学生管理前端控制台，对接现有后端完成查询与维护操作 | Vue 3、Vite、Vue Router、Pinia、Axios、Element Plus |

## 本次前端新增内容

`student-management-web` 是本仓库本次新增的前端工程，主要补齐了学生管理系统的可视化界面与交互流程。

已实现能力：

- 学生分页列表展示
- 姓名模糊查询
- 年龄区间筛选
- 新增学生
- 编辑学生
- 删除学生
- 学生详情抽屉
- 请求失败与参数错误提示
- Vite 代理转发 `/api` 请求到后端服务

## 前端技术栈

| 技术 | 作用 |
| --- | --- |
| Vue 3 | 组件化 UI 开发 |
| Vite | 本地开发与生产构建 |
| Vue Router | 路由管理，默认入口为 `/students` |
| Pinia | 学生列表、筛选条件、分页状态统一管理 |
| Axios | 封装 HTTP 请求与响应拦截 |
| Element Plus | 表格、表单、弹窗、抽屉等基础组件 |

## 运行方式

1. 启动后端项目 `student-management-system`，确保服务运行在 `http://localhost:8888`
2. 进入前端目录并安装依赖
3. 启动开发服务器

```bash
cd student-management-web
npm install
npm run dev
```

启动后可访问：

- 前端页面：`http://localhost:5173/students`
- 前端代理接口前缀：`/api`

## 测试与验证

截至 2026-03-30，前端新增内容已完成以下验证：

- `student-management-web` 执行 `npm run build` 通过
- 与现有后端完成联调验证
- 已实际验证列表查询、新增、详情、编辑、删除整条操作链路



## 文档入口

- 前端详细说明见 [student-management-web/README.md](./student-management-web/README.md)


