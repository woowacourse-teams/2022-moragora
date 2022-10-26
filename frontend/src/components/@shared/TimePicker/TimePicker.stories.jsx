import TimePicker from '.';

export default {
  title: 'Components/TimePicker',
  component: TimePicker,
};

const Template = (args) => {
  return <TimePicker {...args} />;
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
