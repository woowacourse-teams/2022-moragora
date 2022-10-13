import EmailConfirmModal from '.';

export default {
  title: 'Components/EmailConfirmModal',
  component: EmailConfirmModal,
};

const Template = (args) => {
  return <EmailConfirmModal {...args} />;
};

export const Default = Template.bind({});
Default.args = {};
