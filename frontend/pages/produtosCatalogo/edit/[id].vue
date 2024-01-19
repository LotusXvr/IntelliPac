<template>
  <Navbar />
  <div v-if="produto">
    <h2 v-once>Editar produto - {{ produto.nome }}</h2>

    <form @submit.prevent="updateProduto">
      <label for="nome">Nome:</label>
      <input v-model.trim="produtoForm.nome" type="text" />
      <span v-if="produtoForm.nome !== null && !isNameValid" class="error">
        ERRO: {{ formFeedback.nome }}</span>
      <br>
      <label for="peso">Peso: </label>
      <input id="peso" v-model.trim="produtoForm.peso" />
      <span class="error">{{ formFeedback.peso }}</span>
      <br />
      Embalagens a criar:

      <span v-for="embalagem in tiposEmbalagemPrimaria">
        <br />
        <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemPrimariaSelected &&
          !produtoForm.embalagensACriar.includes(embalagem.id)
          " />
        {{ tipoNumeroParaString(embalagem.tipo) }}: {{ embalagem.material }}
      </span>
      <br>
      <span class="error" v-if="!isEmbalagemSelected">
        ERRO: {{ formFeedback.embalagensACriar }}
      </span>
      <br />
      <span v-for="embalagem in tiposEmbalagemSecundaria">
        <br />
        <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemSecundariaSelected &&
          !produtoForm.embalagensACriar.includes(embalagem.id)
          " />
        {{ tipoNumeroParaString(embalagem.tipo) }}: {{ embalagem.material }}
      </span>
      <br />
      <span v-for="embalagem in tiposEmbalagemTercearia">
        <br />
        <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemTerceariaSelected &&
          !produtoForm.embalagensACriar.includes(embalagem.id)
          " />
        {{ tipoNumeroParaString(embalagem.tipo) }}: {{ embalagem.material }}
      </span>
      <br />
      <button type="submit" :disabled="!isFormValid">Save</button>
    </form>
    <nuxt-link to="/produtosCatalogo">Back to produtos</nuxt-link>
  </div>
  <h2>Error messages:</h2>
  {{ messages }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
import Navbar from "../layouts/nav-bar.vue"
import { useAuthStore } from "../store/auth-store.js"

const authStore = useAuthStore()
const { user } = authStore
const route = useRoute();
const id = route.params.id;
const config = useRuntimeConfig();
const api = config.public.API_URL;

const produto = ref(null);
const messages = ref([]);

const produtoForm = reactive({
  nome: null,
  peso: null,
  fabricanteUsername: user.username,
  embalagensACriar: [],
});

const formFeedback = reactive({
  nome: "",
  peso: "",
  embalagensACriar: "",
});

const { data: embalagensACriar } = await useFetch(`${api}/tipoEmbalagens`, {
  method: "GET",
  headers: { "Authorization": "Bearer " + authStore.token }
})

const tiposEmbalagemPrimaria = computed(() => {
  return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 1)
})

const tiposEmbalagemSecundaria = computed(() => {
  return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 2)
})

const tiposEmbalagemTercearia = computed(() => {
  return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 3)
})

const isTipoEmbalagemPrimariaSelected = computed(() => {
  return produtoForm.embalagensACriar.some((id) => {
    const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
    return embalagem.tipo == 1
  })
})

const isTipoEmbalagemSecundariaSelected = computed(() => {
  return produtoForm.embalagensACriar.some((id) => {
    const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
    return embalagem.tipo == 2
  })
})

const isTipoEmbalagemTerceariaSelected = computed(() => {
  return produtoForm.embalagensACriar.some((id) => {
    const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
    return embalagem.tipo == 3
  })
})

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

const fetchProduto = async () => {
  try {
    const response = await fetch(`${api}/produtosCatalogo/${id}`, { method: "GET", headers: { 'Authorization': 'Bearer ' + authStore.token } });
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    produto.value = await response.json();
    produtoForm.nome = produto.value.nome;
    produtoForm.fabricanteUsername = produto.value.fabricanteUsername;
    produtoForm.embalagensACriar = produto.value.embalagensACriar.map((embalagem) => embalagem.id);
    produtoForm.peso = produto.value.peso;
  } catch (error) {
    messages.value.push(error.message);
  }
};

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

const isFabricanteValid = computed(() => {
  if (produtoForm.fabricanteUsername === null) {
    return false;
  }

  formFeedback.fabricanteUsername = "";
  return true;
});

const isPesoValid = computed(() => {
  if (produtoForm.peso !== null && !isNaN(produtoForm.peso) && produtoForm.peso > 0) {
    formFeedback.peso = "";
    return true;
  }
  formFeedback.peso = "O peso tem de ser maior que 0";
  return false;
});

const isEmbalagemSelected = computed(() => {
  console.log(isTipoEmbalagemPrimariaSelected.value)
  if (!isTipoEmbalagemPrimariaSelected.value) {
    formFeedback.embalagensACriar = "Escolha pelo menos uma embalagem primária"
    return false
  }
  formFeedback.embalagensACriar = ""
  return true
})

const isFormValid = computed(() => {
  return isNameValid.value && isFabricanteValid.value && isPesoValid.value && produtoForm.embalagensACriar.length > 0 && isEmbalagemSelected.value;
});

const updateProduto = async () => {
  try {
    const requestBody = {
      nome: produtoForm.nome,
      fabricanteUsername: produtoForm.fabricanteUsername,
      peso: produtoForm.peso,
      embalagensACriar: produtoForm.embalagensACriar.map((id) => ({ id })),
    }

    const requestOptions = {
      method: "PUT",
      headers: { 
        "Content-Type": "application/json",
        'Authorization': 'Bearer ' + authStore.token
     },
      body: JSON.stringify(requestBody),
    };

    const response = await fetch(`${api}/produtosCatalogo/${id}`, requestOptions);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    navigateTo("/produtosCatalogo");
  } catch (error) {
    messages.value.push(error.message);
  }
};

fetchProduto();
</script>
