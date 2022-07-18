import MemberAddInput from '.';

export default {
  title: 'Components/MemberAddInput',
  component: MemberAddInput,
};

const Template = (args) => {
  return <MemberAddInput {...args} />;
};

export const Default = Template.bind({});
Default.args = {};

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
