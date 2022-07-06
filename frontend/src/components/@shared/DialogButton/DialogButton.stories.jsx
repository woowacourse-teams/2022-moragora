import DialogButton from '.';

export default {
  title: 'Components/DialogButton',
  component: DialogButton,
};

const Template = (args) => {
  return <DialogButton {...args} />;
};

export const Confirm = Template.bind({});
Confirm.args = {
  children: '확인',
  variant: 'confirm',
};

export const Dismiss = Template.bind({});
Dismiss.args = {
  children: '취소',
  variant: 'dismiss',
};

export const Disabled = Template.bind({});
Disabled.args = {
  children: '확인',
  disabled: true,
};
