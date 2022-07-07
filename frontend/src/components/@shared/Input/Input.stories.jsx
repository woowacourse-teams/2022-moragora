import Input from '.';

export default {
  title: 'Components/Input',
  component: Input,
};

const Template = (args) => {
  return <Input {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  placeholder: 'placeholder 예시입니다.',
};

export const Disabled = Template.bind({});
Disabled.args = {
  placeholder: 'placeholder 예시입니다.',
  disabled: true,
};
