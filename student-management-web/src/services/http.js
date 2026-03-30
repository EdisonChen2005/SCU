import axios from 'axios'

const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || '/api',
  timeout: 10000,
})

http.interceptors.response.use(
  (response) => {
    const payload = response.data
    if (payload?.code === 200) {
      return payload.data
    }

    return Promise.reject(payload || { message: '请求失败' })
  },
  (error) => {
    const payload = error.response?.data
    if (payload) {
      return Promise.reject(payload)
    }

    return Promise.reject({
      code: 500,
      message: error.message || '网络连接失败',
      errors: null,
    })
  },
)

export default http
