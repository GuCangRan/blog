import { Button, Form, input } from 'ant-design-vue'

export default {
  install(app) {
    app.use(Button)
    .use(Form)
    .use(input);
  }
}