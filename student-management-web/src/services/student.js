import http from './http'

export function getStudentPage(params) {
  return http.get('/student/page', { params })
}

export function getStudentById(id) {
  return http.get(`/student/get/${id}`)
}

export function createStudent(data) {
  return http.post('/student/add', data)
}

export function updateStudent(data) {
  return http.put('/student/update', data)
}

export function deleteStudentById(id) {
  return http.delete(`/student/delete/${id}`)
}
