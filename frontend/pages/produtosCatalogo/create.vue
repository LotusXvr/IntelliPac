<template>
  <form @submit.prevent="create">
    <label for="username">Nome: </label>
    <input id="username" v-model="produtoForm.nome" />
    <span class="error">{{ formFeedback.nome }}</span>
    <br />
    <label for="peso">Peso: </label>
    <input id="peso" v-model="produtoForm.peso">
    <span class="error">{{ formFeedback.peso }}</span>
    <br />
    

    <br />
    <button type="submit" :disabled="!isFormValid">Criar produto</button>
  </form>
  {{ message }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
import { ref, reactive, computed } from "vue";
import { useAuthStore } from "../store/auth-store.js"

const auhtStore = useAuthStore()
const produtoForm = reactive({
  nome: null,
  fabricanteUsername: auhtStore.user.username, // Alterado de Number para aceitar nulos
  peso: null,
});

const formFeedback = reactive({
  nome: "",
  fabricanteUsername: "",
  peso: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const message = ref("");

const isNameValid = computed(() => {
  if (produtoForm.nome === null) {
    return false;
  }
  if (produtoForm.nome.length < 3) {
    formFeedback.nome = "O nome deve ter pelo menos 3 caracteres";
    return false;
  }
  if (produtoForm.nome.length > 20) {
    formFeedback.nome = "O nome deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.nome = "";
  return true;
});

const isPesoValid = computed(() => {
  if (produtoForm.peso !== null && !isNaN(produtoForm.peso) && produtoForm.peso > 0){
    formFeedback.peso = "";
    return true;
    
  }
  formFeedback.peso = "O peso tem de ser maior que 0"
  return false;
})

const isFormValid = computed(() => {
  return isNameValid.value && isPesoValid.value;
});

async function create() {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(produtoForm),
  };
  
  const { error } = await useFetch(`${api}/produtosCatalogo`, requestOptions);
  if (!error.value) navigateTo("/produtosCatalogo");
  message.value = error.value;
}
</script>
