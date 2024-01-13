import { defineStore } from "pinia"
export const useAuthStore = defineStore("authStore", () => {
    const token = ref(null)
    const user = ref(null)
    function logout() {
        token.value = null
        user.value = null
    }
    function login(token, user) {
        token.value = token
        user.value = user
    }

    return { token, user, logout, login}
})
