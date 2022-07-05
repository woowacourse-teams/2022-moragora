import MobileLayout from '../../components/layouts/MobileLayout';
import Header from '../../components/layouts/Header';
import DiscussionPostPage from '.';

export default {
  title: 'Pages/DiscussionPostPage',
  component: DiscussionPostPage,
};

const Template = (args) => {
  return (
    <MobileLayout>
      <Header />
      <DiscussionPostPage {...args} />
    </MobileLayout>
  );
};

export const Default = Template.bind({});
