import TextArea from '.';

export default {
  title: 'Components/TextArea',
  component: TextArea,
};

const Template = (args) => {
  return <TextArea {...args} />;
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
