<template>
  <Navbar />
  <form @submit.prevent="create">

    <label for="nome">Nome: </label>
    <input id="nome" v-model="fabricanteForm.nome" />
    <span class="error" v-if="!isNameValid">{{ formFeedback.nome }}</span>
    <br>

    <label for="username">Username: </label>
    <input id="username" v-model="fabricanteForm.username" />
    <span class="error" v-if="!isUsernameValid">{{ formFeedback.username }}</span>
    <br>

    <label for="email">Email: </label>
    <input id="email" type="email" v-model="fabricanteForm.email" />
    <span class="error" v-if="!isEmailValid">{{ formFeedback.email }}</span>
    <br>

    <label for="password">Password: </label>
    <input id="password" type="password" v-model="fabricanteForm.password" />
    <span class="error" v-if="!isPasswordValid">{{ formFeedback.password }}</span>
    <br>

    <button type="submit" :disabled="!isFormValid">Criar fabricante</button>
  </form>
  {{ message }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
import Navbar from "~/layouts/nav-bar.vue";
import { ref, reactive, computed } from "vue";
const fabricanteForm = reactive({
  nome: null,
  username: null,
  email: null,
  password: null,
});

const formFeedback = reactive({
  nome: "",
  username: "",
  email: "",
  password: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const message = ref("");

const isNameValid = computed(() => {
  if (fabricanteForm.nome === null) {
    return false;
  }
  if (fabricanteForm.nome.length < 3) {
    formFeedback.nome = "ERRO: O nome deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.nome.length > 20) {
    formFeedback.nome = "ERRO: O nome deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.nome = "";
  return true;
});

const isUsernameValid = computed(() => {
  if (fabricanteForm.username === null) {
    return false;
  }
  if (fabricanteForm.username.length < 3) {
    formFeedback.username = "ERRO: O username deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.username.length > 20) {
    formFeedback.username = "ERRO: O username deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.username = "";
  return true;
});

const isEmailValid = computed(() => {
  if (fabricanteForm.email === null) {
    return false;
  }
  if (fabricanteForm.email.length < 3) {
    formFeedback.email = "ERRO: O email deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.email.length > 20) {
    formFeedback.email = "ERRO: O email deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.email = "";
  return true;
});

const isPasswordValid = computed(() => {
  if (fabricanteForm.password === null) {
    return false;
  }
  if (fabricanteForm.password.length < 3) {
    formFeedback.password = "ERRO: A password deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.password.length > 20) {
    formFeedback.password = "ERRO: A password deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.password = "";
  return true;
});


const isFormValid = computed(() => {
  return isNameValid.value && isUsernameValid.value && isEmailValid.value && isPasswordValid.value;
});

async function create() {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json", 'Authorization': 'Bearer ' + authStore.token },
    body: JSON.stringify(fabricanteForm),
  };

  const { error } = await useFetch(`${api}/fabricantes`, requestOptions);
  if (!error.value) navigateTo("/fabricantes");
  message.value = error.value;
}
</script>
