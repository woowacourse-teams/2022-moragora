import Button from '.';

export default {
  title: 'Components/Button',
  component: Button,
};

const Template = (args) => {
  return <Button {...args} />;
};

export const Default = Template.bind({});
Default.args = {
  children: '제출',
};

export const Disabled = Template.bind({});
Disabled.args = {
  children: '제출',
  disabled: true,
};
