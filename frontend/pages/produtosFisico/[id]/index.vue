<template>
    <div v-if="produtoFisico">
      <h2>Detalhes de {{ produtoFisico.nome }}</h2>
      <p>Id: {{ produtoFisico.id }}</p>
      <p>Fabricante Username: {{ produtoFisico.fabricanteUsername }}</p>
      <p>Peso: {{ produtoFisico.peso }}</p>
      <p v-if="produtoFisico.embalagensDeProduto.length == 0"> <b>Sem Embalagens de Produto</b></p>
      <p v-else>Embalagens De Produto:</p>
      <ul>
        <li v-for="embalagem in produtoFisico.embalagensDeProduto" :key="embalagem.id">
          {{ tipoEmbalagemString(embalagem.tipoEmbalagem) }} - {{ embalagem.material }}
          <ul>
            <li v-for="sensor in embalagem.sensores">
                {{ sensor.tipo }} - {{ sensor.unidade }}
                        <br />
                        <nuxt-link :to="`/sensores/${sensor.id}`">Ver mais detalhes</nuxt-link>
                        <br />
                        <div v-if="sensor.observacoes.length > 0">
                            Ultima observação:
                            <ul>
                                <li>
                                    {{ sensor.observacoes[0].timestamp }} - {{ sensor.observacoes[0].valor }} {{ sensor.unidade }}
                                </li>
                            </ul>
                            
                        </div>
                        <div v-else>
                            <h4>Ainda nenhuma observação registada</h4>
                        </div>
            </li>
          </ul>
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
  const { data: produtoFisico, error: proErr } = await useFetch(
    `${api}/produtosFisicos/${id}`
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
  