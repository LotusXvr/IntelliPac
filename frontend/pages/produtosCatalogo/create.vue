<template>
  <form @submit.prevent="create">
    <Navbar />

    <label for="username">Nome: </label>
    <input id="username" v-model="produtoForm.nome" />
    <span class="error">{{ formFeedback.nome }}</span>
    <br />
    <label for="peso">Peso: </label>
    <input id="peso" v-model="produtoForm.peso">
    <span class="error">{{ formFeedback.peso }}</span>
    <br />
    Embalagens a criar:
    <p v-for="tipo in tiposEmbalagem">
      <span v-for="embalagem in embalagensACriar">
        <div v-if="tipo == embalagem.tipo">
          <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar"
            @click="desativarRestantes(embalagem.tipo, embalagem.id)">
          {{ tipoNumeroParaString(embalagem.tipo) }}: {{ embalagem.material }}
        </div>

      </span>
    </p>

    <!-- <span v-if="!isEmbalagemSelected" class="error">
      ERRO: {{ formFeedback.embalagensACriar }}</span> -->

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
import Navbar from "~/layouts/nav-bar.vue"
import { ref, reactive, computed } from "vue";
import { useAuthStore } from "../store/auth-store.js"

const auhtStore = useAuthStore()
const { user } = auhtStore

const produtoForm = reactive({
  nome: null,
  fabricanteUsername: user.username,
  peso: null,
  embalagensACriar: [],
});

const formFeedback = reactive({
  nome: "",
  fabricanteUsername: "",
  peso: "",
  embalagensACriar: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const message = ref("");

const { data: embalagensACriar } = useFetch(`${api}/tipoEmbalagens`);

const tiposEmbalagem = [1, 2, 3];

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
  if (produtoForm.peso !== null && !isNaN(produtoForm.peso) && produtoForm.peso > 0) {
    formFeedback.peso = "";
    return true;

  }
  formFeedback.peso = "O peso tem de ser maior que 0"
  return false;
})

const isFormValid = computed(() => {
  return isNameValid.value && isPesoValid.value;
});

const tipoNumeroParaString = (tipo) => {
  switch (tipo) {
    case 1:
      return "Primária"
    case 2:
      return "Secundária"
    case 3:
      return "Terceária"
    default:
      return "Tipo desconhecido"
  }
}

async function create() {
  const requestBody = {
    nome: produtoForm.nome,
    fabricanteUsername: produtoForm.fabricanteUsername,
    peso: produtoForm.peso,
    embalagensACriar: produtoForm.embalagensACriar.map((id) => ({ id }))
  }
  console.log("requestBody")
  console.log(requestBody)

  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(requestBody),
  };

  const { error } = await useFetch(`${api}/produtosCatalogo`, requestOptions);
  if (!error.value) navigateTo("/produtosCatalogo");
  message.value = error.value;
}
</script>
