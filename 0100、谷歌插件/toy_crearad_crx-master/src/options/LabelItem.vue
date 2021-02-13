<template>
  <div class="label-item">
    <div v-if="label" class="label">{{label}}</div>
    <div class="lab">
      <slot></slot>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'LabelItem',
    props: {
      label: String
    }
  }
</script>

<style scoped>
.label {
  height: 56px;
  line-height: 56px;
  color: #616161;
  text-align: left;
  font-size: 15px;
  font-weight: 700;
}
.lab {
  padding: 10px;
  width: 100%;
  background-color: #fff;
  border-radius: 2px;
  /* box-shadow: 0 1px 3px rgba(0,0,0,.12); */
}
</style>