import InputHint from '.';

export default {
  title: 'Components/InputHint',
  component: InputHint,
};

const Template = (args) => {
  return <InputHint {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  isShow: true,
  message: 'message',
};
