<template>
    <div v-if="produtoCatalogo">
      <h2>Detalhes de {{ produtoCatalogo.nome }}</h2>
      <p>Id: {{ produtoCatalogo.id }}</p>
      <p>Fabricante Username: {{ produtoCatalogo.fabricanteUsername }}</p>
      <p>Peso: {{ produtoCatalogo.peso }}</p>
      <p>Produtos:</p>
      <ul>
        <li v-for="produto in produtoCatalogo.produtos" :key="produto.id">
          {{ produto.nome }}
        </li>
      </ul>
      <p>Embalagens do Produto:</p>
      <ul>
        <li v-for="embalagem in produtoCatalogo.embalagensACriar" :key="embalagem.id">
          {{ tipoEmbalagemString(embalagem.tipo) }} - {{ embalagem.material }}
        </li>
      </ul>
    </div>
  
    <h2>Error messages:</h2>
    {{ messages }}
  </template>
  <script setup>
  const route = useRoute();
  const id = route.params.id;
  const config = useRuntimeConfig();
  const api = config.public.API_URL;
  const { data: produtoCatalogo, error: proErr } = await useFetch(
    `${api}/produtosCatalogo/${id}`
  );
  const messages = ref([]);
  if (proErr.value) messages.value.push(proErr.value);

  const tipoEmbalagemString = (tipo) => {
    switch (tipo) {
      case 1:
        return 'Primaria';
      case 2:
        return 'Secundaria';
      case 3:
        return 'Terciaria';
    }
  };
  </script>
  