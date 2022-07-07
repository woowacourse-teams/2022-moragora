import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import MeetingPage from '.';

export default {
  title: 'Pages/MeetingPage',
  component: MeetingPage,
};

const Template = (args) => {
  return (
    <MobileLayout>
      <Header />
      <MeetingPage {...args} />
    </MobileLayout>
  );
};

export const Default = Template.bind({});
