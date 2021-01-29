<template>
  <a-form
    class="options-form"
    ref="dynamicValidateForm"
    :model="dynamicValidateForm"
    v-bind="formItemLayoutWithOutLabel"
  >
    <a-form-item
      v-for="(domain, index) in dynamicValidateForm.domains"
      :key="domain.key"
      :name="['domains', index, 'value']"
      :rules="{
        required: true,
        message: '网址不能为空！',
        trigger: 'change',
      }"
    >
      <a-input
        v-model:value="domain.value"
        placeholder="请输入网址"
        style="width: 60%; margin-right: 8px"
      />
      <MinusCircleOutlined
        v-if="dynamicValidateForm.domains.length > 1"
        class="dynamic-delete-button"
        :disabled="dynamicValidateForm.domains.length === 1"
        @click="removeDomain(domain)"
      />
    </a-form-item>
    <a-form-item v-bind="formItemLayoutWithOutLabel">
      <a-button type="dashed" style="width: 60%" @click="addDomain">
        <PlusOutlined />
      </a-button>
    </a-form-item>
    <a-form-item v-bind="formItemLayoutWithOutLabel">
      <a-button type="primary" html-type="submit" @click="submitForm('dynamicValidateForm')">
        保存
      </a-button>
      <a-button style="margin-left: 10px" @click="resetForm('dynamicValidateForm')">
        重置
      </a-button>
    </a-form-item>
  </a-form>
</template>

<script>
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons-vue';
import config from '../config';
import { getStore, setStore } from '../utils';
// let id = 0;
export default {
  components: {
    MinusCircleOutlined,
    PlusOutlined,
  },
  data() {
    return {
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
      },
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 24, offset: 0 },
          sm: { span: 20, offset: 4 },
        },
      },
      dynamicValidateForm: {
        domains: [],
      },
    };
  },
  created() {
    getStore(config.S_KEY).then(res => {
      this.dynamicValidateForm.domains = res ? res.reduce((arr, v, i) => {
        arr.push({
          key: i,
          value: v
        });
        return arr;
      }, []) : []
    })
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName]
        .validate()
        .then(() => {
          const values = this.dynamicValidateForm.domains;
          const msg = values.reduce((arr, v) => {
            arr.push(v.value)
            return arr;
          }, [])
          setStore(config.S_KEY, msg)
        })
        .catch(error => {
          console.log('error', error);
        });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removeDomain(item) {
      let index = this.dynamicValidateForm.domains.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.domains.splice(index, 1);
      }
    },
    addDomain() {
      this.dynamicValidateForm.domains.push({
        value: '',
        key: Date.now(),
      });
    },
  },
};
</script>
<style>

.options-form {
  width: 800px;
}
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
