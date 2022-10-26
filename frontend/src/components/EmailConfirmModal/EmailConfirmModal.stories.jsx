import EmailConfirmModal from '.';

export default {
  title: 'Components/EmailConfirmModal',
  component: EmailConfirmModal,
};

const Template = (args) => {
  return <EmailConfirmModal {...args} />;
};

const timer = new Date();
timer.setMinutes(timer.getMinutes() + 5);

export const Default = Template.bind({});
Default.args = {
  email: 'user1@google.com',
  expiredTimestamp: timer.getTime(),
  onSuccess: () => {},
  onDismiss: () => {},
  refetch: () => {},
};
