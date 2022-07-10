import ReloadButton from '.';

export default {
  title: 'Components/ReloadButton',
  component: ReloadButton,
};

const Template = (args) => {
  return <ReloadButton {...args} />;
};

export const Default = Template.bind({});

export const Disabled = Template.bind({});
Disabled.args = {
  disabled: true,
};
