import { defineStore } from "pinia"
export const useAuthStore = defineStore("authStore", () => {
    const token = ref(null)
    const user = ref(null)
    function logout() {
        token.value = null
        user.value = null
    }
    function login(newToken, newUser) {
        token.value = newToken
        user.value = newUser
    }

    return { token, user, logout, login }
})
